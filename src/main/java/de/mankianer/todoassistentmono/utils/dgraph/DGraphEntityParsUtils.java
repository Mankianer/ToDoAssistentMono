package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

public class DGraphEntityParsUtils {

  static {
    gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
    gson = DGraphEntityParsUtils.gsonBuilder.create();
  }

  private static Map<Class<? extends DgraphMultiClassEntity>, Function<String, Class<? extends DgraphMultiClassEntity>>> resolverMap = new HashMap<>();
  private static GsonBuilder gsonBuilder;
  @Getter
  private static Gson gson;

  public static <S extends DgraphEntity> void registerJsonDeserializer(Class<S> parent,
      JsonDeserializer<S> jsonDeserializer) {
    gsonBuilder.registerTypeAdapter(parent, jsonDeserializer);
    gson = gsonBuilder.create();
  }

  public static <S extends DgraphEntity> List<S> parsJson(String json, Class<S> actualTypeArgument) {
    S[] entities = gson.fromJson(json, (Type) actualTypeArgument.arrayType());
    return Arrays.asList(entities);
  }

  /**
   * only test purpose
   */
  public static void reset() {
    gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
    gson = DGraphEntityParsUtils.gsonBuilder.create();
  }
}
