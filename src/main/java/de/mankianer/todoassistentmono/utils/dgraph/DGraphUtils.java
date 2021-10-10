package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtils;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DGraphUtils {

  public static Class<? extends DgraphMultiClassEntity> findMultiClassParent(
      Class clazz) {
    Class<?> possibleParent = clazz;
    while (possibleParent.getSuperclass() != null) {
      if (possibleParent.getSuperclass().equals(DgraphMultiClassEntity.class)) {
        return (Class<? extends DgraphMultiClassEntity>) possibleParent;
      }
      possibleParent = possibleParent.getSuperclass();
    }
    return null;
  }

  public static String parseMultiClassIdentifierFromJson(String json) {
    JsonReader jsonReader = new Gson().newJsonReader(new StringReader(json));
    String identifier = null;
    while (true) {
      try {
        if (!jsonReader.hasNext()) {
          break;
        }
        JsonToken token = jsonReader.peek();
        if (JsonToken.NAME.equals(token)) {
          if (jsonReader.nextName().equalsIgnoreCase("multiClassIdentifier")) {
            JsonToken possibleIdentifier = jsonReader.peek();
            if (JsonToken.STRING.equals(possibleIdentifier)) {
              identifier = jsonReader.nextString();
              break;
            }
          }
        }

        if (JsonToken.BEGIN_ARRAY.equals(token)) {
          jsonReader.beginArray();
        } else if (JsonToken.END_ARRAY.equals(token)) {
          jsonReader.endArray();
        } else if (JsonToken.BEGIN_OBJECT.equals(token)) {
          jsonReader.beginObject();
        } else if (JsonToken.END_OBJECT.equals(token)) {
          jsonReader.endObject();
        } else {
          jsonReader.skipValue();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return identifier;
  }

  public static Map<String, Class<? extends DgraphMultiClassEntity>> findMultiClassWithPath(
      Class targetClass, String pathPrefix, Function<String, String> findIdentifierByPath) {
    Map<String, Class<? extends DgraphMultiClassEntity>> ret = new HashMap<>();
    try {
      Object instance = null;
      try {
        instance = targetClass.getDeclaredConstructor().newInstance();
      } catch (NoSuchMethodException | InstantiationException e) {
        Class<? extends DgraphMultiClassEntity> multiClassParent = findMultiClassParent(
            targetClass);
        if (multiClassParent != null) {
          instance = blankInstandOfMultiClassParent(multiClassParent);
        }
      }
      if (instance instanceof DgraphEntity) {
        if (instance instanceof DgraphMultiClassEntity) {
          Class childClass = DgraphRepo.TryResolveMultiClassEntity(
              findIdentifierByPath.apply(pathPrefix),
              targetClass);
          ret.put(pathPrefix,
              childClass != null ? childClass : DgraphMultiClassEntity.class);
          try {
            instance = childClass.getDeclaredConstructor().newInstance();
          } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            instance = blankInstandOfMultiClassParent(findMultiClassParent(childClass));
          }
        }
        List<Field> allFields = ((DgraphEntity) instance).getAllFields();
        allFields.forEach(field -> {
          if (Arrays.stream(field.getDeclaredAnnotations()).filter(
                  annotation -> "JsonIgnore".equals(annotation.annotationType().getSimpleName()))
              .findFirst().isPresent()) {
            return;
          }
          Class fieldClass = DGraphQueryUtils.convertFieldToClass(field);
          ret.putAll(
              findMultiClassWithPath(fieldClass, pathPrefix + "." + field.getName(),
                  findIdentifierByPath));
        });
      }
    } catch (Exception e) {
      log.error(e);
    }
    return ret;
  }

  public static DgraphMultiClassEntity blankInstandOfMultiClassParent(
      @NonNull Class<? extends DgraphMultiClassEntity> multiClassParent) {
    DgraphMultiClassEntity instance;
    try {
      instance = multiClassParent.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
      instance = new DgraphMultiClassEntity();
    }

    return instance;
  }
}
