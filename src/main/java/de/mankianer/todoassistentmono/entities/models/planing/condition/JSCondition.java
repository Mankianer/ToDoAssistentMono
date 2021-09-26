package de.mankianer.todoassistentmono.entities.models.planing.condition;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
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
@EqualsAndHashCode(callSuper=false)
public class JSCondition extends DgraphEntity implements Condition {

  private static String functionName = "isTrue";

  @NonNull
  private String name, script;

  /**
   *
   * @param context
   * @return
   * @throws ConditionException
   * rootException Map:
   *  ScriptException = Script Error
   *  NoSuchMethodException = Script Error with semantic-problems
   *  NullPointerException = Blank Script
   *  Exception = Unknown Error
   */
  @Override
  public boolean evaluate(ConditionContext context) throws ConditionException {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
    try {
      engine.eval("function " + functionName + "(context) {\n" +
          script +
          "\n}");
      functionName = "isTrue";
      Boolean aBoolean = (Boolean) ((Invocable) engine).invokeFunction(functionName, context);
      return aBoolean;
    } catch (ScriptException e) {
      throw new ConditionException("Script Error", e);
    } catch (NoSuchMethodException e) {
      throw new ConditionException("Internal Error caused by Script", e);
    } catch (NullPointerException e) {
      if( ("" + script).isBlank() ) throw new ConditionException("Script is Empty", e);
      throw new ConditionException("Internal Error caused by Script", e);
    } catch (Exception e) {
      throw new ConditionException("Unknown Error", e);
    }
  }
}
