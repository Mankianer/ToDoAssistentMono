package de.mankianer.todoassistentmono.entities.models.planing.condition;

@FunctionalInterface
public interface DayProfileConditionInterface {
  boolean evaluate(DayProfileConditionContext context) throws DayProfileConditionException;
}
