package de.mankianer.todoassistentmono.entities.models.calendarentries;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CalendarEntry for mapping to GoogleCalendar.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class GoogleCalendarEntry extends CalendarEntry {

  public GoogleCalendarEntry() {
    setType(Type.GOOGLE);
  }
}
