package de.mankianer.todoassistentmono.utils.dgraph.query;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQueryRootFilter.RootTypes;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DGraphQueryUtils {

  public static DQuery createFindByValueQuery(String filedName, String paramName,
      Class<? extends DgraphEntity> actualTypeArgument, Map<String, Map> queryMap)
      throws NoSuchFieldException {
    return DQuery.builder().queryname("findByValue").functionName("findByValue")
        .actualTypeArgument(actualTypeArgument).fieldName(filedName).paramName(paramName)
        .queryMap(queryMap)
        .paramType(findDGraphType(actualTypeArgument.getDeclaredField(filedName).getType()))
        .rootFilter(
            RootTypes.EQUALS).build();
  }

  public static String createQueryString(String fields, String queryname,
      String queryfunctionname) {
    return "query " + queryname + "($uid: string) {\n"
        + queryfunctionname + "(func: uid($uid)) {\n"
        + fields +
        """
              }
            }""";
  }


  /**
   * Maps a Class<?> to a DGraphType for Query
   */
  public static DGraphType findDGraphType(Class<?> clazz) {
    if (clazz.equals(Integer.class)) {
      return DGraphType.INT;
    } else if (clazz.equals(Float.class) || clazz.equals(Double.class)) {
      return DGraphType.FLOAT;
    } else if (clazz.equals(Boolean.class)) {
      return DGraphType.BOOLEAN;
    } else if (clazz.equals(LocalDateTime.class) || clazz.equals(LocalDate.class)) {
      return DGraphType.DATETIME;
    }
    return DGraphType.DEFAULT;
  }

  /**
   *
   */
  public static String convertQueryMapToField(Map<String, Map> queryMap) {
    final String[] queryString = {""};
    queryMap.entrySet().forEach(stringMapEntry -> {
      queryString[0] += stringMapEntry.getKey();
      if (stringMapEntry.getValue() != null) {
        queryString[0] += "\n{\n" + convertQueryMapToField(stringMapEntry.getValue()) + "}";
      }
      queryString[0] += "\n";
    });

    return queryString[0];
  }

  /**
   * gets Class of a Class-Field if it's a List it returns the Type of the List.
   *
   * @return correct Type of DgraphEntity Field of Object or List
   */
  public static Class<?> convertFieldToClass(Field field) {
    Class<?> fieldClass = field.getType();
    if (field.getType().equals(List.class)) {
      ParameterizedType listType = (ParameterizedType) field.getGenericType();
      fieldClass = (Class<?>) listType.getActualTypeArguments()[0];
    }
    return fieldClass;
  }

  /**
   * Mapping the Fields of a DgraphEntity for Query in a treeLike Map. Ignorse Fileds with
   *
   * @return Map<S, Map < S, Map < S, . . .>>> if Key is not null it is a DgraphEntity
   * @JsonIgnore annotation.
   */
  public static Map<String, Map> getFieldMap(Class<? extends DgraphEntity> clazz,
      Map<String, Class<? extends DgraphMultiClassEntity>> pathToMultiClassMap, String path) {
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
            if(fieldInstance instanceof DgraphMultiClassEntity){
              Class<? extends DgraphMultiClassEntity> aClass = pathToMultiClassMap.get(path);
              if (aClass != null) {
                fieldClass = aClass;
              }
            }
            fieldMap.put(field.getName(), getFieldMap(
                (Class<? extends DgraphEntity>) fieldClass, pathToMultiClassMap, path + "." + field.getName()));
          } else {
            fieldMap.put(field.getName(), null);
          }
        } catch (Exception e) {
          fieldMap.put(field.getName(), null);
        }

      });
    } catch (Exception e) {
      System.err.println("Error while parsing DGraphEntity to DgraphQuery, not critical!");
      System.err.println(e);
      fieldMap.put("uid", null);
    }
    return fieldMap;
  }

  public static Map<String, Map> createValueMapForMultiClassIdentifier(String path) {
    Map<String, Map> valueMap = new HashMap<>();
    valueMap.put("multiClassIdentifier", null);
    System.out.println("add multiClassIdentifier");
    //baut valueMap für Path auf
    if (path != null && !path.isBlank()) {
      String[] split = path.split("\\.");
      for (int i = split.length - 1; i >= 0; i--) {
        Map<String, Map> newSubMap = new HashMap<>();
        newSubMap.put(split[i], valueMap);
        valueMap = newSubMap;
        System.out.println("add " + split[i]);
      }
    }
    return valueMap;
  }
}
