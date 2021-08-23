package de.mankianer.todoassistentmono.entities.interfaces;

public interface CalendarEntry {
  Type getType();

  String getID();

  public enum Type {
    GOOGLE, UNKNOWN;
  }
}
