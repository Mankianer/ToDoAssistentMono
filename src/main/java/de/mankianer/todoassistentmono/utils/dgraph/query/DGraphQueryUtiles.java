package de.mankianer.todoassistentmono.utils.dgraph.query;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQueryRootFilter.RootTypes;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DGraphQueryUtiles {

  public static DQuery createFindByValueQuery(String filedName, String paramName, Class<? extends DgraphEntity> actualTypeArgument)
      throws NoSuchFieldException {
    return DQuery.builder().queryname("findByValue").functionName("findByValue")
        .actualTypeArgument(actualTypeArgument).fieldName(filedName).paramName(paramName)
        .paramType(findDGraphType(actualTypeArgument.getField(filedName).getType())).rootFilter(
            RootTypes.EQUALS).build();
  }

  public static DGraphType findDGraphType(Type type) {
    if (type.getTypeName().equals(Integer.class.getTypeName())) {
      return DGraphType.INT;
    } else if (type.getTypeName().equals(Float.class.getTypeName())) {
      return DGraphType.FLOAT;
    } else if (type.getTypeName().equals(LocalDateTime.class.getTypeName()) || type.getTypeName()
        .equals(LocalDate.class.getTypeName())) {
      return DGraphType.DATETIME;
    } else if (type.getTypeName().equals(Boolean.class.getTypeName())) {
      return DGraphType.BOOLEAN;
    }
    return DGraphType.DEFAULT;
  }

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

  public static Class<?> convertFieldToClass(Field field) {
    Class<?> fieldClass = field.getType();
    if (field.getType().equals(List.class)) {
      ParameterizedType listType = (ParameterizedType) field.getGenericType();
      fieldClass = (Class<?>) listType.getActualTypeArguments()[0];
    }
    return fieldClass;
  }

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
