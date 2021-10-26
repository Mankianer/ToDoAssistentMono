package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class DQueryFilterFunctionCompare extends DQueryFilterFunction {

  @NonNull
  private String fieldName;

  @NonNull
  private RootTypes rootTypes;

  @Override
  public String buildFilterFunctionString() {
    return rootTypes.getName() + "(" + fieldName + ", $" + paramName + ")";
  }

  public enum RootTypes {
    EQUALS("eq"),
    GREATER_OR_EQUALS("ge"),
    GREATER("gt"),
    LESS_OR_EQUALS("le"),
    LESS("lt");

    @Getter
    private String name;

    RootTypes(String name) {

      this.name = name;
    }
  }
}
