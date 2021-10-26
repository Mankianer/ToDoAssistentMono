package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;

@Builder
public class DQueryFilterFunctionUid extends DQueryFilterFunction {

  public DQueryFilterFunctionUid() {
    super(DGraphType.STRING);
  }

  @Override
  public String buildFilterFunctionString() {
    return "uid($" + paramName + ")";
  }
}
