package de.mankianer.todoassistentmono.entities.models.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionContext;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionException;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Default DayProfileRule for access DGraphObject.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DayProfileRule extends DgraphEntity {

  private int prio;
  private DayProfile dayProfile;
  private DayProfileCondition dayProfileCondition;

  public boolean evaluate() throws DayProfileConditionException {
    return dayProfileCondition.evaluate(new DayProfileConditionContext());
  }

}
