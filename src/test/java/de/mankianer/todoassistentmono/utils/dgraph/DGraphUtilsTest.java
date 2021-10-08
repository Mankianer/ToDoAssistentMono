package de.mankianer.todoassistentmono.utils.dgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import org.junit.jupiter.api.Test;

class DGraphUtilsTest {

  @Test
  public void findMultiClassParent(){
    assertEquals(DayProfileCondition.class, DGraphUtils.findMultiClassParent(JSDayProfileCondition.class));
    assertEquals(DayProfileCondition.class, DGraphUtils.findMultiClassParent(DayProfileCondition.class));
    assertNull(DGraphUtils.findMultiClassParent(Object.class));
  }

}
