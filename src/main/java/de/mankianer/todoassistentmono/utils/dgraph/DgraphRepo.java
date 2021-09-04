package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    String fields = convertQueryMapToString(getQueryMap());
    String queryname = "findByUid";
    String query = "query " + queryname + "($uid: string) {\n"
                    + queryname + "(func: uid($uid)) {\n"
                    + fields +
                      """
                      }
                    }""";
    Map<String, String> vars = Collections.singletonMap("$uid", uid);
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(query, vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring(("{\"" + queryname + "\":").length(), json.length() - 1);
    log.debug("response Json:{}", json);
    T[] byUid = gson.fromJson(json, (Type) actualTypeArgument.arrayType());

    return byUid.length > 0 ? byUid[0] : null;
  }

  private String convertQueryMapToString(Map<String, Map> queryMap){
    final String[] queryString = {""};
    queryMap.entrySet().forEach(stringMapEntry -> {
      queryString[0] += stringMapEntry.getKey();
      if(stringMapEntry.getValue() != null){
        queryString[0] += "\n{\n" +  convertQueryMapToString(stringMapEntry.getValue()) + "}";
      }
      queryString[0] += "\n";
    });

    return queryString[0];
  }

  private Map getQueryMap() {
    return getFieldMap(actualTypeArgument);
  }

  private Map<String, Map> getFieldMap(Class<? extends DgraphEntity> clazz) {
    HashMap<String, Map> fieldMap = new HashMap<>();
    try {

      var instance = clazz.getDeclaredConstructor().newInstance();
      List<Field> allFields = instance
          .getAllFields();
      allFields.forEach(field -> {
        if (Arrays.stream(field.getDeclaredAnnotations()).filter(
                annotation -> "JsonIgnore".equals(annotation.annotationType().getSimpleName()))
            .findFirst().isPresent()) {
          return;
        }
        Class<?> fieldClass = convertFieldToClass(field);
        try {
          var fieldInstance = fieldClass.getDeclaredConstructor().newInstance();
          if (fieldInstance instanceof DgraphEntity) {
            fieldMap.put(field.getName(), getFieldMap(
                (Class<? extends DgraphEntity>) fieldInstance.getClass()));
          } else {
            fieldMap.put(field.getName(), null);
          }
        } catch (Exception e) {
          fieldMap.put(field.getName(), null);
        }

      });
    } catch (Exception e) {
      log.warn(e);
      fieldMap.put("uid", null);
    }
    return fieldMap;
  }

  private Class<?> convertFieldToClass(Field field) {
    Class<?> fieldClass = field.getType();
    if (field.getType().equals(List.class)) {
      ParameterizedType listType = (ParameterizedType) field.getGenericType();
      fieldClass = (Class<?>) listType.getActualTypeArguments()[0];
    }
    return fieldClass;
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
