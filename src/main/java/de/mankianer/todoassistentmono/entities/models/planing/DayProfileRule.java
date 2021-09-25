package de.mankianer.todoassistentmono.entities.models.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.planing.condition.Condition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.ConditionContext;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DayProfileRule {

  private int prio;
  private DayProfile dayProfile;
  private Condition condition;

  public boolean evaluate() {
    return condition.evaluate(new ConditionContext());
  }

}
