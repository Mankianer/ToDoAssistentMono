package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ValueException;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.DayOfMonthDayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.repos.DayProfileConditionRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileConditionController {

  private Map<String, Class<? extends DayProfileCondition>> dayProfileConditionMap;

  private Map<String, Map<String, ParameterType>> conditionParameterMap;

  @Getter
  private final DayProfileConditionRepo repo;

  public DayProfileConditionController(DayProfileConditionRepo repo) {
    DgraphRepo.registerMultiCastEntityResolver(DayProfileCondition.class, this::resolve);
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

  public DayProfileCondition createNewCondition(String identifier, Map<String, ?> values)
      throws ValueException {
    try {
      DayProfileCondition dayProfileCondition = resolve(identifier).getDeclaredConstructor()
          .newInstance();
      dayProfileCondition.applyValues(values);
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
}
