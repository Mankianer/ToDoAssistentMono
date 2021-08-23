package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.CalendarEntry;
import de.mankianer.todoassistentmono.entities.interfaces.TimeSlotInterface;
import de.mankianer.todoassistentmono.entities.interfaces.ToDoFilter;
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
public class TimeSlot extends DGraphEntity implements TimeSlotInterface {

  @NonNull
  private String name;
  @NonNull
  private ToDoFilter toDoFilter;
  @NonNull
  private LocalDateTime start;
  private LocalDateTime end;
  private CalendarEntry calenderEntry;
  private TimeSlotInterface next, previous;
  private String id;


}
