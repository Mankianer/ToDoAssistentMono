package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class DQueryRootFilter {
  @NonNull
  private String fieldName, paramName;
  @NonNull
  private RootTypes rootTypes;

  public String buildRootFilterString() {
    return "func: " + rootTypes.name + "(" + fieldName + ", $" + paramName + ")";
  }

  public enum RootTypes {
    EQUALS("eq"),
    GREATER_OR_EQUALS("ge"),
    GREATER("gt"),
    LESS_OR_EQUALS("le"),
    LESS("lt");

    private String name;

    RootTypes(String name) {

      this.name = name;
    }
  }
}
