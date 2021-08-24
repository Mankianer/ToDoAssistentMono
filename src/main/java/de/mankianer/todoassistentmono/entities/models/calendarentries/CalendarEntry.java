package de.mankianer.todoassistentmono.entities.models.calendarentries;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntryInterface;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import lombok.Data;

@Data
public class CalendarEntry extends DgraphEntity implements CalendarEntryInterface {

  private String ID;
  private Type type;
}
