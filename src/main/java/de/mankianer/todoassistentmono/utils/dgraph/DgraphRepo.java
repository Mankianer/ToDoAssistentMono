package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtiles;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphType;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQuery;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DgraphRepo<T extends DgraphEntity> {

  private final Class<T> actualTypeArgument;
  private DgraphClient dgraphClient;
  private Gson gson;

  public DgraphRepo(DgraphClient dgraphClient) {
    this.dgraphClient = dgraphClient;
    this.gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();
    actualTypeArgument = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public T saveToDGraph(T entity) {
    Transaction txn = dgraphClient.newTransaction();
    try {
      String json = gson.toJson(entity);
      Mutation mutation = Mutation.newBuilder().setSetJson(ByteString.copyFromUtf8(json.toString()))
          .setCommitNow(true)
          .build();

      Response response = txn.mutate(mutation);
      Collection<String> uids = response.getUidsMap().values();
      String topLevelUid = response.getUidsMap().entrySet().stream()
          .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey())).map(entry -> entry.getValue())
          .findFirst().orElse(null);
      if (topLevelUid != null) {
        entity = findByUid(topLevelUid);
      }
      return entity;
    } finally {
      txn.discard();
    }

  }

  private String findMutationID(String mutionObjectID) {
    String[] split = mutionObjectID.split("\\.");
    if (split.length != 3) {
      return null;
    }
    return split[0] + "." + split[1];
  }

  public T findByUid(String uid) {
    String fields = DGraphQueryUtiles.convertQueryMapToField(getQueryMap());
    String queryname = "findByUid";
    String queryfunctionname = queryname;
    String query = "query " + queryname + "($uid: string) {\n"
        + queryfunctionname + "(func: uid($uid)) {\n"
        + fields +
        """
              }
            }""";
    Map<String, String> vars = Collections.singletonMap("$uid", uid);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(query, vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring(("{\"" + queryfunctionname + "\":").length(), json.length() - 1);
    log.debug("response Json:{}", json);
    T[] byUid = gson.fromJson(json, (Type) actualTypeArgument.arrayType());

    return byUid.length > 0 ? byUid[0] : null;
  }

  public T findByValue(String name, String value, DGraphType type) throws NoSuchFieldException {
//    String fields = convertQueryMapToString(getQueryMap());
//    String queryname = "findByUid";
//    String queryfunctionname = queryname;
//    String query = "query " + queryname + "($" + name + ": " + type.name + ") {\n"
//        + queryfunctionname + "(func: eq(" + name + ", $" + name + ")) {\n"
//        + fields +
//        """
//              }
//            }""";
    DQuery findByValueQuery = DGraphQueryUtiles.createFindByValueQuery(name, name,
        actualTypeArgument);
    Map<String, String> vars = Collections.singletonMap("$" + name, value);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(findByValueQuery.buildQueryString(), vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring(("{\"" + findByValueQuery.getFunctionName() + "\":").length(), json.length() - 1);
    log.debug("response Json:{}", json);
    T[] byUid = gson.fromJson(json, (Type) actualTypeArgument.arrayType());

    return byUid.length > 0 ? byUid[0] : null;
  }

  private Map getQueryMap() {
    return DGraphQueryUtiles.getFieldMap(actualTypeArgument);
  }

  public boolean deleteFromDGraphByUid(@NonNull String uid) {
    T entity = findByUid(uid);
    Transaction txn = dgraphClient.newTransaction();
    try {
      String json = gson.toJson(entity);
      Mutation mutation = Mutation.newBuilder().setDeleteJson(ByteString.copyFromUtf8(json))
          .setCommitNow(true)
          .build();
      Response response = txn.mutate(mutation);
      Collection<String> uids = response.getUidsMap().values();
      if (uids.size() > 1) {
        log.warn("more than 1 uid was returned!");
        response.getUidsMap().forEach((k, v) -> log.warn("{}:{}", k, v));
      }
      entity.setUid(uids.stream().findFirst().orElse(null));
      return true;
    } finally {
      txn.discard();
    }


  }
}
