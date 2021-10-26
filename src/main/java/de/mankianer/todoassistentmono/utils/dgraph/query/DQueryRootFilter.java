package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class DQueryRootFilter {

  @NonNull
  private DQueryFilterFunction rootFilterFunction;

  public String buildRootFilterString() {
    return "func: " + rootFilterFunction.buildFilterFunctionString();
  }
}
