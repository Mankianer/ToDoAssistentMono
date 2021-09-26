package de.mankianer.todoassistentmono.entities.models.planing.condition;

import lombok.Getter;

public class ConditionException extends Exception {

  @Getter
  private Exception rootException;

  public ConditionException(String msg, Exception rootException){
    super(msg, rootException);
    this.rootException = rootException;
  }
}
