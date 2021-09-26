package de.mankianer.todoassistentmono.entities.models.calendarentries;

import lombok.Data;

/**
 * CalendarEntry for mapping to GoogleCalendar.
 */
@Data
public class GoogleCalendarEntry extends CalendarEntry {

  public GoogleCalendarEntry() {
    setType(Type.GOOGLE);
  }
}
