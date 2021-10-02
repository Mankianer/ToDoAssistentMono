package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.DayOfMonthDayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileConditionController {

  private Map<String, Class<? extends DayProfileCondition>> dayProfileConditionMap;
  @Getter
  private Map<String, Map<String, ParameterType>> conditionParameterMap;

  private final DgraphService dgraphService;

  public DayProfileConditionController(DgraphService dgraphService) {
    dayProfileConditionMap = new HashMap<>();
    this.dgraphService = dgraphService;
  }

  @PostConstruct
  public void init() {
    register(JSDayProfileCondition.class, DayOfMonthDayProfileCondition.class);
  }

//  public <T extends DayProfileCondition> T saveToDgraph(T entity) {
//    resolve(entity.getClass().getSimpleName())
//    return dgraphService.<T>saveToDGraph(entity);
//  }
//
//  public <T extends DayProfileCondition> T findByUid(String uid){
//    return dgraphService.findByUid(uid);
//  }
//
//  public boolean deleteFromDGraph(@NonNull String uid) {
//    return dgraphService.deleteFromDGraph(uid);
//  }

  public Collection<Class<? extends DayProfileCondition>> getRegisteredCondition() {
    return dayProfileConditionMap.values();
  }

  public void register(Class<? extends DayProfileCondition>... dayProfileConditionClasses) {
    for (Class<? extends DayProfileCondition> dayProfileConditionClass : dayProfileConditionClasses) {
      dayProfileConditionMap.put(dayProfileConditionClass.getSimpleName(), dayProfileConditionClass);
    }
    conditionParameterMap = createConditionParameterMap();
  }

  public Class<? extends DayProfileCondition> resolve(String identifier) {
    return dayProfileConditionMap.get(identifier);
  }

  public Map<String, Map<String, ParameterType>> createConditionParameterMap() {
    Map<String, Map<String, ParameterType>> ret = new HashMap<>();
    dayProfileConditionMap.entrySet().forEach(e -> {
      DayProfileCondition o = null;
      try {
        o = e.getValue().getConstructor().newInstance();
        ret.put(e.getKey(), o.getParameterTypeMap());
      } catch (Exception ex) {
        log.warn(ex);
        ret.put(e.getKey(), null);
      }
    });
    return ret;
  }
}
