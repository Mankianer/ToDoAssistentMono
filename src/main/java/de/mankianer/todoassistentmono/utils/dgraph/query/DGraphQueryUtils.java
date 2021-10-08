package de.mankianer.todoassistentmono.utils.dgraph.query;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
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

  /**
   * creates a DQuery-Object based on a Class of DgraphEntity
   * @param filedName of the Object
   * @param paramName
   * @param actualTypeArgument
   * @return
   * @throws NoSuchFieldException if fieldName is not a DeclaredField of actualType
   */
  public static DQuery createFindByValueQuery(String filedName, String paramName,
      Class<? extends DgraphEntity> actualTypeArgument)
      throws NoSuchFieldException {
    return createFindByValueQuery(filedName, paramName, actualTypeArgument, getFieldMap(actualTypeArgument));
  }

  public static DQuery createFindByValueQuery(String filedName, String paramName,
      Class<? extends DgraphEntity> actualTypeArgument, Map<String, Map> queryMap)
      throws NoSuchFieldException {
    return DQuery.builder().queryname("findByValue").functionName("findByValue")
        .actualTypeArgument(actualTypeArgument).fieldName(filedName).paramName(paramName).queryMap(queryMap)
        .paramType(findDGraphType(actualTypeArgument.getDeclaredField(filedName).getType())).rootFilter(
            RootTypes.EQUALS).build();
  }

  public static String createQueryString(String fields, String queryname, String queryfunctionname) {
    return "query " + queryname + "($uid: string) {\n"
        + queryfunctionname + "(func: uid($uid)) {\n"
        + fields +
        """
              }
            }""";
  }


  /**
   * Maps a Class<?> to a DGraphType for Query
   * @param clazz
   * @return
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
   * @param queryMap
   * @return
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
   * @param field
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
   * Mapping the Fields of a DgraphEntity for Query in a treeLike Map.
   * Ignorse Fileds with @JsonIgnore annotation.
   * @param clazz
   * @return Map<S, Map<S, Map<S, ...>>> if Key is not null it is a DgraphEntity
   */
  public static Map<String, Map> getFieldMap(Class<? extends DgraphEntity> clazz) {
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
      System.err.println("Error while parsing DGraphEntity to DgraphQuery, not critical!");
      System.err.println(e);
      fieldMap.put("uid", null);
    }
    return fieldMap;
  }
}
