package de.mankianer.todoassistentmono.entities.models.planing.condition;

public interface Condition {
  boolean evaluate(ConditionContext context);
}
