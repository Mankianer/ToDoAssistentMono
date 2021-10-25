package de.mankianer.todoassistentmono.entities.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntryInterface;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default TimeSlot for access DGraphObject.
 * TimeSlot for selecting ToDos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TimeSlot extends DgraphEntity {

  @NonNull
  private String name;
  @NonNull
  private ToDoFilter toDoFilter;
  @NonNull
  private LocalDateTime start;
  private LocalDateTime end;
  private CalendarEntryInterface calenderEntry;

  @com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore
  @JsonIgnore
  private TimeSlot next, previous;

  /**
   *
   * @return end of Timeslot or the Start of TimeSlot.next or the own start
   */
  public LocalDateTime getEnd(){
    return end != null ? end : (next != null ? next.getStart() : start);
  }
}
