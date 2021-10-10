package de.mankianer.todoassistentmono.planing;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ValueException;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.DayOfMonthDayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.repos.DayProfileConditionRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileConditionController implements JsonDeserializer<DayProfileCondition> {

  private Map<String, Class<? extends DayProfileCondition>> dayProfileConditionMap;

  @Getter(AccessLevel.PACKAGE)
  private Map<String, Map<String, ParameterType>> conditionParameterMap;

  @Getter
  private final DayProfileConditionRepo repo;

  public DayProfileConditionController(DayProfileConditionRepo repo) {
    DgraphRepo.registerMultiCastEntityResolver(DayProfileCondition.class, this::resolve, this);
    dayProfileConditionMap = new HashMap<>();
    conditionParameterMap = new HashMap<>();
    this.repo = repo;
  }

  @PostConstruct
  public void init() {
    register(JSDayProfileCondition.class, DayOfMonthDayProfileCondition.class);
  }

  public Set<Entry<String, Map<String, ParameterType>>> getRegisteredConditionParameterMap() {
    return conditionParameterMap.entrySet();
  }

  public void register(Class<? extends DayProfileCondition>... dayProfileConditionClasses) {
    for (Class<? extends DayProfileCondition> dayProfileConditionClass : dayProfileConditionClasses) {
      try {
        DayProfileCondition dayProfileCondition = dayProfileConditionClass.getDeclaredConstructor()
            .newInstance();
        dayProfileConditionMap.put(dayProfileCondition.getMultiClassIdentifier(), dayProfileConditionClass);
        conditionParameterMap.put(dayProfileCondition.getMultiClassIdentifier(),
            dayProfileCondition.getParameterTypeMap());
      } catch (Exception e) {
        log.error(e);
      }
    }
  }

  public Class<? extends DayProfileCondition> resolve(String identifier) {
    return dayProfileConditionMap.get(identifier);
  }

  public DayProfileCondition createNewCondition(String name, String identifier, Map<String, ?> values)
      throws ValueException {
    try {
      DayProfileCondition dayProfileCondition = resolve(identifier).getDeclaredConstructor()
          .newInstance();
      dayProfileCondition.applyValues(values);
      dayProfileCondition.setName(name);
      return dayProfileCondition;
    } catch (InstantiationException e) {
      log.error(e);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    }
    return null;
  }

  @Override
  public DayProfileCondition deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    String multiClassIdentifier = json.getAsJsonObject().entrySet().stream()
        .filter(e -> e.getKey().equalsIgnoreCase("multiClassIdentifier"))
        .map(e -> e.getValue().getAsString()).findFirst().orElse(null);
    Class<? extends DayProfileCondition> resolvedClass = resolve(multiClassIdentifier);
    if(resolvedClass != null){
      DayProfileCondition dayProfileCondition = context.deserialize(json, resolvedClass);
      return dayProfileCondition;
    }
    return null;
  }
}
