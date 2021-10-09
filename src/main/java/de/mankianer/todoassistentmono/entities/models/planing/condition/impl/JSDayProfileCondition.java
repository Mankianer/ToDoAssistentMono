package de.mankianer.todoassistentmono.entities.models.planing.condition.impl;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionContext;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileConditionException;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * A Condition witch is ruled via JavaScript
 */
@Log4j2
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JSDayProfileCondition extends DayProfileCondition {

  private static String functionName = "isTrue";

  @NonNull
  private String script;

  /**
   * @throws DayProfileConditionException rootException Map: ScriptException = Script Error
   * NoSuchMethodException = Script Error with semantic-problems NullPointerException = Blank Script
   * Exception = Unknown Error
   */
  @Override
  public boolean evaluate(DayProfileConditionContext context) throws DayProfileConditionException {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
    try {
      engine.eval("function " + functionName + "(context) {\n" +
          script +
          "\n}");
      functionName = "isTrue";
      Boolean aBoolean = (Boolean) ((Invocable) engine).invokeFunction(functionName, context);
      return aBoolean;
    } catch (ScriptException e) {
      throw new DayProfileConditionException("Script Error", e);
    } catch (NoSuchMethodException e) {
      throw new DayProfileConditionException("Internal Error caused by Script", e);
    } catch (NullPointerException e) {
      if (("" + script).isBlank()) {
        throw new DayProfileConditionException("Script is Empty", e);
      }
      throw new DayProfileConditionException("Internal Error caused by Script", e);
    } catch (Exception e) {
      throw new DayProfileConditionException("Unknown Error", e);
    }
  }

  @Override
  public Map<String, ParameterType> getParameterTypeMap() {
    return Map.of(
        "script", ParameterType.STRING
    );
  }

  @Override
  protected void passValues(Map<String, ?> values) {
    script = (String) values.get("script");
  }
}
