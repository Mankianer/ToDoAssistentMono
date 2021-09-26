package de.mankianer.todoassistentmono.entities.models.planing.condition;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A Condition witch is ruled via JavaScript
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSCondition extends DgraphEntity implements Condition {

  private static String functionName = "isTrue";

  @NonNull
  private String name, script;

  @Override
  public boolean evaluate(ConditionContext context) {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
    try {
      engine.eval("function " + functionName + "(context) {\n" +
          script +
          "\n}");
      functionName = "isTrue";
      Boolean aBoolean = (Boolean) ((Invocable) engine).invokeFunction(functionName, context);
      return aBoolean;
    } catch (ScriptException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return false;
  }
}
