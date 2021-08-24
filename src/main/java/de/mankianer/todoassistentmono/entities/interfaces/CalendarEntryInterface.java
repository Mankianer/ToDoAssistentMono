package de.mankianer.todoassistentmono.entities.interfaces;

public interface CalendarEntryInterface {
  Type getType();

  String getID();

  public enum Type {
    GOOGLE, UNKNOWN;
  }
}
