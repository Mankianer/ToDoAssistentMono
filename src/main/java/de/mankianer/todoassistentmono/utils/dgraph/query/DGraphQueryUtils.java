package de.mankianer.todoassistentmono.utils.dgraph.query;

import de.mankianer.todoassistentmono.utils.dgraph.DGraphUtils;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQueryFilterFunctionCompare.RootTypes;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DGraphQueryUtils {

  /**
   * @param filedName searching DgraphEntity field
   * @param paramName searching DgraphQuery parameter
   * @param queryFieldMap values to be used in Query
   * @return a Query for request Dgraph
   */
  public static DQuery createFindByValueQuery(String filedName, String paramName,
      DGraphType paramType,
      Map<String, Map> queryFieldMap) {
    DQueryRootFilter rootFilter = getQueryRootFilter_FieldEqualParam(filedName, paramName,
        paramType);
    DQueryFunction function = getQueryFunctionByRootFilterAndFunctionName(rootFilter,
        "findByValue");
    return DQuery.builder().queryname("findByValue").queryMap(queryFieldMap).function(function)
        .build();
  }

  /**
   * @param queryFieldMap values to be used in Query
   * @return a Query for request Dgraph
   */
  public static DQuery createFindByUidQuery(Map<String, Map> queryFieldMap) {
    DQueryRootFilter rootFilter = getQueryRootFilter_Uid();
    DQueryFunction function = getQueryFunctionByRootFilterAndFunctionName(rootFilter, "findByUid");
    return DQuery.builder().queryname("findByUid").queryMap(queryFieldMap).function(function)
        .build();
  }

  public static DQueryFunction getQueryFunctionByRootFilterAndFunctionName(
      DQueryRootFilter rootFilter, String functionName) {
    return DQueryFunction.builder().functionName(functionName)
        .queryRootFilter(rootFilter).build();
  }

  public static DQueryRootFilter getQueryRootFilter_FieldEqualParam(String filedName,
      String paramName, DGraphType paramType) {
    DQueryFilterFunctionCompare filterFunction = DQueryFilterFunctionCompare.builder().fieldName(
            filedName)
        .paramName(paramName).paramType(paramType)
        .rootTypes(RootTypes.EQUALS).build();
    DQueryRootFilter rootFilter = DQueryRootFilter.builder().rootFilterFunction(filterFunction)
        .build();
    return rootFilter;
  }

  public static DQueryRootFilter getQueryRootFilter_Uid() {
    DQueryFilterFunctionUid filterFunction = DQueryFilterFunctionUid.builder().fieldName("uid")
        .paramName("uid").build();
    DQueryRootFilter rootFilter = DQueryRootFilter.builder().rootFilterFunction(filterFunction)
        .build();
    return rootFilter;
  }

  /**
   * @param fields fieldspart of the QueryString
   * @param queryname name of Query
   * @param queryfunctionname name of QueryFunction
   * @return the Query as String
   */
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
   * @param queryMap values used to be in Query
   * @return FieldsPart for DGraphQuery
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
   * @JsonIgnore
   * @JsonIgnore annotation.
   */
  public static Map<String, Map> getFieldMap(Class<? extends DgraphEntity> clazz,
      Map<String, Class<? extends DgraphMultiClassEntity>> pathToMultiClassMap, String path) {
    HashMap<String, Map> fieldMap = new HashMap<>();
    try {
      DgraphEntity instance = null;
      try {
        instance = clazz.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        Class<? extends DgraphMultiClassEntity> multiClassParent = DGraphUtils.findMultiClassParent(
            clazz);
        if (multiClassParent != null) {
          instance = DGraphUtils.blankInstandOfMultiClassParent(multiClassParent);
        }
      }
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
            if (fieldInstance instanceof DgraphMultiClassEntity) {
              Class<? extends DgraphMultiClassEntity> aClass = pathToMultiClassMap.get(path);
              if (aClass != null) {
                fieldClass = aClass;
              }
            }
            fieldMap.put(field.getName(), getFieldMap(
                (Class<? extends DgraphEntity>) fieldClass, pathToMultiClassMap,
                path + "." + field.getName()));
          } else {
            fieldMap.put(field.getName(), null);
          }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
          fieldMap.put(field.getName(), null);
        }

      });
    } catch (RuntimeException e) {
      log.error("Error while parsing DGraphEntity to DgraphQuery, not critical!", e);
      fieldMap.put("uid", null);
    }
    return fieldMap;
  }

  /**
   * @param path target path for Identifier
   * @return QueryMap From Path with MultiClassIdentifierQuery
   */
  public static Map<String, Map> createQueryMapFromPathForMultiClassIdentifier(String path) {
    Map<String, Map> valueMap = new HashMap<>();
    valueMap.put("multiClassIdentifier", null);
    //baut valueMap für Path auf
    if (path != null && !path.isBlank()) {
      String[] split = path.split("\\.");
      for (int i = split.length - 1; i >= 0; i--) {
        Map<String, Map> newSubMap = new HashMap<>();
        newSubMap.put(split[i], valueMap);
        valueMap = newSubMap;
      }
    }
    return valueMap;
  }
}
