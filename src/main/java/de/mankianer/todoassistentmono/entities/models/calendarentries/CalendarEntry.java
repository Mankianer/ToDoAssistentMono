package de.mankianer.todoassistentmono.entities.models.calendarentries;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntryInterface;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import lombok.Data;
import lombok.NonNull;

@Data
public class CalendarEntry extends DgraphEntity implements CalendarEntryInterface {

  private String ID;
  @NonNull
  private Type type = Type.UNKNOWN;
}
