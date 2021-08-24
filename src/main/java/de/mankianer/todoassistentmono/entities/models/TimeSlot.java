package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntryInterface;
import de.mankianer.todoassistentmono.entities.interfaces.TimeSlotInterface;
import de.mankianer.todoassistentmono.entities.interfaces.ToDoFilterInterface;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TimeSlot extends DgraphEntity implements TimeSlotInterface {

  @NonNull
  private String name;
  @NonNull
  private ToDoFilterInterface toDoFilter;
  @NonNull
  private LocalDateTime start;
  private LocalDateTime end;
  private CalendarEntryInterface calenderEntry;
  private TimeSlotInterface next, previous;

}
