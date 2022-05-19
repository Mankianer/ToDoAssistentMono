package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtils;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DGraphMultiClassEntityUtils {

  private static Map<Class<? extends DgraphMultiClassEntity>, Function<String, Class<? extends DgraphMultiClassEntity>>> resolverMap = new HashMap<>();

  public static <S extends DgraphMultiClassEntity> void registerMultiCastEntityResolver(
      Class<S> parent, Function<String, Class<? extends DgraphMultiClassEntity>> resolver, JsonDeserializer<S> jsonDeserializer) {
    resolverMap.put(parent, resolver);
    DGraphEntityParsUtils.registerJsonDeserializer(parent, jsonDeserializer);
  }

  public static <S extends DgraphMultiClassEntity> Class<? extends DgraphMultiClassEntity> tryResolveMultiClassEntity(
      String identifier, Class<S> targetClass) {
    if (targetClass == null || identifier == null) {
      return null;
    }
    Class<? extends DgraphMultiClassEntity> multiClassParent = findMultiClassParent(
        targetClass);
    Function<String, Class<? extends DgraphMultiClassEntity>> stringClassFunction = resolverMap.get(
        multiClassParent);
    return stringClassFunction != null ? stringClassFunction.apply(identifier) : null;
  }

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

  /**
   * @param pathPrefix prefix for Path
   * @param findIdentifierByPath Method to resolve Path to actually MultiClassEntityIdentifier
   * @return Map: FieldClass Path to DgraphMultiClassParent if Field is instanceOf
   * DgraphMultiClassEntity
   */
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
          childClass = childClass != null ? childClass : DgraphMultiClassEntity.class;
          ret.put(pathPrefix,
              childClass);
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

  public static Map<String, Class<? extends DgraphMultiClassEntity>> findMultiClassWithPath(
      Class<? extends DgraphEntity> targetClass, Function<String, String> findIdentifierByPath) {
    return findMultiClassWithPath(targetClass, "", findIdentifierByPath);
  }

  /**
   *
   * @param multiClassParent
   * @return instance of multiClassParent or DgraphMultiClassEntity
   */
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

  /**
   * @param json returned after request MulticlassIdentifier
   * @return Map(Uid -> MultiClassIdentifier)
   */
  public static Map<String, String> parseMultiClassIdentifierFromJson(String json) {
    return DGraphEntityParsUtils.parsJson(json,
        DgraphMultiClassEntity.class).stream().collect(Collectors.toMap(m -> m.getUid(), m -> m.getMultiClassIdentifier()));
  }

  /**
   * only test purpose
   */
  public static void reset() {
    resolverMap = new HashMap<>();
  }
}
