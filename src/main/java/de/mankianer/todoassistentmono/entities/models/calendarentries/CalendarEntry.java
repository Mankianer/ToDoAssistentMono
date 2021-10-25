package de.mankianer.todoassistentmono.entities.models.calendarentries;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntryInterface;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Default CalenderEntry for access CalendarEntry-object in DGraph.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CalendarEntry extends DgraphEntity implements CalendarEntryInterface {

  private String ID;
  @NonNull
  private Type type = Type.UNKNOWN;
}
