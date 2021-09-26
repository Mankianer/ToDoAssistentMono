package de.mankianer.todoassistentmono.entities.models.planing.condition;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JSConditionTest {


  @ValueSource(strings = {"return true"})
  @ParameterizedTest
  void evaluateTrue(String script) throws ConditionException {
    JSCondition jsCondition = new JSCondition("test", script);
    assertTrue(jsCondition.evaluate(new ConditionContext()));
  }
  @ValueSource(strings = {"return false"})
  @ParameterizedTest
  void evaluateFalse(String script) throws ConditionException {
    JSCondition jsCondition = new JSCondition("test", script);
    assertFalse(jsCondition.evaluate(new ConditionContext()));
  }

  @ValueSource(strings = {
      "return fal",
      ""
  })
  @ParameterizedTest
  void evaluateError(String script) throws ConditionException {
    JSCondition jsCondition = new JSCondition("test", script);
    assertThrows(ConditionException.class, () -> {
      jsCondition.evaluate(new ConditionContext());
    });
  }

}
