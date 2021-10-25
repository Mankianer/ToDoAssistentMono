package de.mankianer.todoassistentmono.entities.models.planing.condition.impl;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionContext;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DayOfMonthDayProfileCondition extends DayProfileCondition {

  static private ParameterType number;

  static {
    DayOfMonthDayProfileCondition.number = ParameterType.NUMBER;
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < 32; i++) {
      list.add(i);
    }
    DayOfMonthDayProfileCondition.number.setAllowedValues(list);
  }

  @NonNull
  private int dayOfMonth = 0;

  @Override
  public Map<String, ParameterType> getParameterTypeMap() {
    return Map.of(
        "dayOfMonth", DayOfMonthDayProfileCondition.number
    );
  }

  @Override
  protected void passValues(Map<String, ?> values) {
    this.dayOfMonth = getIntegerFromNumber("dayOfMonth", values);
  }

  @Override
  public boolean evaluate(DayProfileConditionContext context) throws DayProfileConditionException {
    return context.getDate().getDayOfMonth() == dayOfMonth;
  }
}
