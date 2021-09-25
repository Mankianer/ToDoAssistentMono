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
      boolean funcResult = (boolean) ((Invocable) engine).invokeFunction(functionName, context);
    } catch (ScriptException | NoSuchMethodException e) {
      e.printStackTrace();
    }

    return false;
  }
}
