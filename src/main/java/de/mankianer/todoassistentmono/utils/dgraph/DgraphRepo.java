package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtils;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DgraphRepo<T extends DgraphEntity> {

  static {
    gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
    gson = DgraphRepo.gsonBuilder.create();
  }

  private static Map<Class<? extends DgraphMultiClassEntity>, Function<String, Class<? extends DgraphMultiClassEntity>>> resolverMap = new HashMap<>();
  private static GsonBuilder gsonBuilder;
  private static Gson gson;

  private final Class<T> actualTypeArgument;
  private final Class<? extends DgraphMultiClassEntity> multiClassParent;
  private DgraphClient dgraphClient;

  public static <S extends DgraphMultiClassEntity> void registerMultiCastEntityResolver(
      Class<S> parent, Function<String, Class<? extends DgraphMultiClassEntity>> resolver, JsonDeserializer<S> jsonDeserializer) {
    resolverMap.put(parent, resolver);
    gsonBuilder.registerTypeAdapter(parent, jsonDeserializer);
    gson = gsonBuilder.create();
  }

  public static <S extends DgraphMultiClassEntity> Class<? extends DgraphMultiClassEntity> TryResolveMultiClassEntity(
      String identifier, Class<S> targetClass) {
    if (targetClass == null || identifier == null) {
      return null;
    }
    Class<? extends DgraphMultiClassEntity> multiClassParent = DGraphUtils.findMultiClassParent(
        targetClass);
    Function<String, Class<? extends DgraphMultiClassEntity>> stringClassFunction = resolverMap.get(
        multiClassParent);
    return stringClassFunction != null ? stringClassFunction.apply(identifier) : null;
  }

  public DgraphRepo(DgraphClient dgraphClient) {
    this.dgraphClient = dgraphClient;
    actualTypeArgument = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    multiClassParent = DGraphUtils.findMultiClassParent(
        (Class<? extends DgraphMultiClassEntity>) actualTypeArgument);
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
    Map queryMap = getQueryMap(actualTypeArgument,
        getFindMultiClassIdentifierByPathFunktionByUid(uid));
    String json = findJsonByUidAndQueryMap(uid, queryMap);

    T[] byUid = gson.fromJson(json, (Type) actualTypeArgument.arrayType());
    return byUid.length > 0 ? byUid[0] : null;
  }

  public T findByValue(String name, String value, DGraphType type)
      throws NoSuchFieldException {
    Map queryMap = getQueryMap(actualTypeArgument,
        getFindMultiClassIdentifierByPathFunktionByValue(name, value, type));
    String json = findJsonByValueAndQueryMap(name, value, type, actualTypeArgument, queryMap);

    T[] byUid = gson.fromJson(json, (Type) actualTypeArgument.arrayType());
    return byUid.length > 0 ? byUid[0] : null;
  }

  private Class<? extends DgraphMultiClassEntity> tryResolveMultiClassEntity(
      String multiClassIdentifier) {
    return DgraphRepo.resolverMap.get(multiClassParent).apply(multiClassIdentifier);
  }

  private String findJsonByUidAndQueryMap(String uid, Map<String, Map> queryMap) {
    String fields = DGraphQueryUtils.convertQueryMapToField(queryMap);
    String queryname = "findByUid";
    String queryfunctionname = queryname;
    String query = DGraphQueryUtils.createQueryString(fields, queryname, queryname);
    Map<String, String> vars = Collections.singletonMap("$uid", uid);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(query, vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring(("{\"" + queryfunctionname + "\":").length(), json.length() - 1);
    log.debug("response Json:{}", json);
    return json;
  }

  private Function<String, String> getFindMultiClassIdentifierByPathFunktionByUid(String uid) {
    return (String path) -> {
      return findMultiClassIdentifierByUid(uid, path);
    };
  }

  private Function<String, String> getFindMultiClassIdentifierByPathFunktionByValue(String name,
      String value, DGraphType type) {
    return (String path) -> {
      try {
        return findMultiClassIdentifierByValue(name, value, type, path);
      } catch (NoSuchFieldException e) {
        log.error(e);
        return null;
      }
    };
  }

  private String findMultiClassIdentifierByUid(String uid, String path) {
    Map<String, Map> valueMap = DGraphQueryUtils.createQueryMapFromPathForMultiClassIdentifier(
        path);
    String json = findJsonByUidAndQueryMap(uid, valueMap);
    String identifier = DGraphUtils.parseMultiClassIdentifierFromJson(json);
    return identifier;
  }

  private String findMultiClassIdentifierByValue(String name, String value, DGraphType type,
      String path)
      throws NoSuchFieldException {
    Map<String, Map> valueMap = DGraphQueryUtils.createQueryMapFromPathForMultiClassIdentifier(
        path);
    String json = findJsonByValueAndQueryMap(name, value, type, actualTypeArgument, valueMap);
    String identifier = DGraphUtils.parseMultiClassIdentifierFromJson(json);
    return identifier;
  }

  private String findJsonByValueAndQueryMap(String name, String value, DGraphType type,
      Class<? extends DgraphEntity> actualTypeArgument, Map<String, Map> queryMap)
      throws NoSuchFieldException {
    DQuery findByValueQuery = DGraphQueryUtils.createFindByValueQuery(name, name,
        actualTypeArgument, queryMap);
    Map<String, String> vars = Collections.singletonMap("$" + name, value);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(findByValueQuery.buildQueryString(), vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring(("{\"" + findByValueQuery.getFunctionName() + "\":").length(),
        json.length() - 1);
    log.debug("response Json:{}", json);
    return json;
  }

  private Map getQueryMap(Class<? extends DgraphEntity> targetClass,
      Function<String, String> findIdentifierByPath) {
    Map<String, Class<? extends DgraphMultiClassEntity>> pathToMultiClassMap = findMultiClassWithPath(
        targetClass, findIdentifierByPath);

    Class<? extends DgraphMultiClassEntity> correctTargetClass = DgraphRepo.TryResolveMultiClassEntity(
        findIdentifierByPath.apply(""),
        DGraphUtils.findMultiClassParent(targetClass));
    return DGraphQueryUtils.getFieldMap(
        correctTargetClass != null ? correctTargetClass : targetClass, pathToMultiClassMap, "");
  }

  private Map<String, Class<? extends DgraphMultiClassEntity>> findMultiClassWithPath(
      Class<? extends DgraphEntity> targetClass, Function<String, String> findIdentifierByPath) {
    return DGraphUtils.findMultiClassWithPath(targetClass, "", findIdentifierByPath);
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

  public boolean isMultiClass() {
    return multiClassParent != null;
  }
}
