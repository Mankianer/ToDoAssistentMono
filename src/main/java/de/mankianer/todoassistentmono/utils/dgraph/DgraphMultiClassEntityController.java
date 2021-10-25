package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DgraphMultiClassEntityController<T extends DgraphMultiClassEntity> implements
    JsonDeserializer<T> {

  @Getter
  private final Class<T> actualTypeArgument;
  @Getter
  private final DgraphRepo<T> repo;
  private Map<String, Class<? extends T>> resolverMap;

  public DgraphMultiClassEntityController(DgraphRepo<T> repo) {
    actualTypeArgument = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    DgraphRepo.registerMultiCastEntityResolver(actualTypeArgument, this::resolve, this);
    resolverMap = new HashMap<>();
    this.repo = repo;
  }

  public void register(Class<? extends T>... classes) {
    for (Class<? extends T> clazz : classes) {
      try {
        T instance = clazz.getDeclaredConstructor()
            .newInstance();
        resolverMap.put(instance.getMultiClassIdentifier(), clazz);
        onRegister(instance, clazz);
      } catch (Exception e) {
        log.error(e);
      }
    }
  }

  public Class<? extends T> resolve(String identifier) {
    return resolverMap.get(identifier);
  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    String multiClassIdentifier = json.getAsJsonObject().entrySet().stream()
        .filter(e -> e.getKey().equalsIgnoreCase("multiClassIdentifier"))
        .map(e -> e.getValue().getAsString()).findFirst().orElse(null);
    Class<? extends T> resolvedClass = resolve(multiClassIdentifier);
    if (resolvedClass != null) {
      T instance = context.deserialize(json, resolvedClass);
      return instance;
    }
    return null;
  }

  protected void onRegister(T instance, Class<? extends T> clazz) {

  }
}
