package de.mankianer.todoassistentmono.entities.models.calendarentries;

import lombok.Data;

@Data
public class GoogleCalendarEntry extends CalendarEntry {

  public GoogleCalendarEntry() {
    setType(Type.GOOGLE);
  }
}
