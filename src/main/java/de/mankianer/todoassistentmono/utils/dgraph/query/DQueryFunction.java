package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class DQueryFunction {

  @NonNull
  private String functionName;

  @NonNull
  private DQueryRootFilter queryRootFilter;


  public String buildFunctionString() {
    return functionName + " (" + queryRootFilter.buildRootFilterString() + ")";
  }


}
