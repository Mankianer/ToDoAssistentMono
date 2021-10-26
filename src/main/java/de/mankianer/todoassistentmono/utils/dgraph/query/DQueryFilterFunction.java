package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class DQueryFilterFunction {

  @NonNull
  protected String paramName;
  @NonNull
  protected DGraphType paramType;

  public DQueryFilterFunction(
      @NonNull DGraphType paramType) {
    this.paramType = paramType;
  }

  public abstract String buildFilterFunctionString();
}
