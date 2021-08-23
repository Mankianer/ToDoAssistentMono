package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntry;
import lombok.Data;

@Data
public class GoogleCalendarEntry implements CalendarEntry {

  private String ID;

  @Override
  public Type getType() {
    return Type.GOOGLE;
  }
}
