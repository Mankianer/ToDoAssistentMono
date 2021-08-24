package de.mankianer.todoassistentmono.entities.models.calendarentries;

import lombok.Data;

@Data
public class GoogleCalendarEntry extends CalendarEntry {

  @Override
  public Type getType() {
    return Type.GOOGLE;
  }
}
