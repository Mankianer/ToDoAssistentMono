package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ValueException;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.DayOfMonthDayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.repos.DayProfileConditionRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphMultiClassEntityController;
import java.lang.reflect.InvocationTargetException;
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
public class DayProfileConditionController extends
    DgraphMultiClassEntityController<DayProfileCondition> {

  @Getter(AccessLevel.PACKAGE)
  private Map<String, Map<String, ParameterType>> conditionParameterMap;


  public DayProfileConditionController(DayProfileConditionRepo repo) {
    super(repo);
    conditionParameterMap = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    register(JSDayProfileCondition.class, DayOfMonthDayProfileCondition.class);
  }

  public Set<Entry<String, Map<String, ParameterType>>> getRegisteredConditionParameterMap() {
    return conditionParameterMap.entrySet();
  }

  @Override
  protected void onRegister(DayProfileCondition dayProfileCondition, Class clazz) {
    conditionParameterMap.put(dayProfileCondition.getMultiClassIdentifier(),
        dayProfileCondition.getParameterTypeMap());
  }

  public DayProfileCondition createNewCondition(String name, String identifier,
      Map<String, ?> values)
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
}
