package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class DQueryFilterFunctionUid extends DQueryFilterFunction {

  @NonNull
  private String fieldName, paramName;

  private DGraphType paramType = DGraphType.STRING;

  @Override
  public String buildFilterFunctionString() {
    return "uid($" + paramName + ")";
  }
}
