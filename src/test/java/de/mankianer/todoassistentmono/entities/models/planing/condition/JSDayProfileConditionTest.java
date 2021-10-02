package de.mankianer.todoassistentmono.entities.models.planing.condition;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JSDayProfileConditionTest {


  @ValueSource(strings = {"return true"})
  @ParameterizedTest
  void evaluateTrue(String script) throws DayProfileConditionException {
    JSDayProfileCondition jsCondition = new JSDayProfileCondition("test", script);
    assertTrue(jsCondition.evaluate(new DayProfileConditionContext()));
  }
  @ValueSource(strings = {"return false"})
  @ParameterizedTest
  void evaluateFalse(String script) throws DayProfileConditionException {
    JSDayProfileCondition jsCondition = new JSDayProfileCondition("test", script);
    assertFalse(jsCondition.evaluate(new DayProfileConditionContext()));
  }

  @ValueSource(strings = {
      "return fal",
      ""
  })
  @ParameterizedTest
  void evaluateError(String script) throws DayProfileConditionException {
    JSDayProfileCondition jsCondition = new JSDayProfileCondition("test", script);
    assertThrows(DayProfileConditionException.class, () -> {
      jsCondition.evaluate(new DayProfileConditionContext());
    });
  }

}
