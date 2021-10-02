package de.mankianer.todoassistentmono.entities.models.planing.condition;

import lombok.Getter;

public class DayProfileConditionException extends Exception {

  @Getter
  private Exception rootException;

  public DayProfileConditionException(String msg, Exception rootException){
    super(msg, rootException);
    this.rootException = rootException;
  }
}
