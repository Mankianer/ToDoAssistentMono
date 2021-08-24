package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DgraphRepo<T extends DgraphEntity> {

  private DgraphClient dgraphClient;
  private Gson gson;

  public DgraphRepo(DgraphClient dgraphClient) {
    this.dgraphClient = dgraphClient;
    this.gson = new Gson();
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
      if (uids.size() > 1) {
        log.warn("more than 1 uid was returned!");
        response.getUidsMap().forEach((k, v) -> log.warn("{}:{}", k, v));
      }
      entity.setUid(uids.stream().findFirst().orElse(null));
      return entity;
    } finally {
      txn.discard();
    }

  }

  public T[] findByUid(String uid, Class<T> clazz) {
    String fields = "";
    try {
      fields = clazz.getDeclaredConstructor().newInstance().getAllFields().stream()
          .map(field -> {
            return field.getName() + "\n";
          }).collect(Collectors.joining());
    } catch (Exception e) {
      log.warn(e);
      fields = "uid";
    }
    String query = """
        query findByUid($uid: string) {
           findByUid(func: uid($uid)) {
           """
        + fields +
        """
           }
         }""";
    Map<String, String> vars = Collections.singletonMap("$uid", uid);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(query, vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring("{\"findByUid\":".length(), json.length() - 1);
    log.info("response Json:" + json);
    T[] byUid = gson.fromJson(json, (Type) clazz.arrayType());
    return byUid;
  }
}
