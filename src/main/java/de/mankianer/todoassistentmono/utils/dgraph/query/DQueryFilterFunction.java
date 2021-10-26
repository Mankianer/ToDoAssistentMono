package de.mankianer.todoassistentmono.utils.dgraph.query;

import lombok.Getter;

@Getter
public abstract class DQueryFilterFunction {

  public abstract String buildFilterFunctionString();

  public abstract String getParamName();

  public abstract DGraphType getParamType();
}
