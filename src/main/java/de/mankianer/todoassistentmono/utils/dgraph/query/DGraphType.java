package de.mankianer.todoassistentmono.utils.dgraph.query;

public enum DGraphType {
  STRING("string"),
  INT("int"),
  FLOAT("float"),
  DATETIME("dateTime"),
  DEFAULT("default"),
  GEO("geo"),
  BOOLEAN("bool");

  private String name;

  private DGraphType(String name) {
    this.name = name;
  }
}
