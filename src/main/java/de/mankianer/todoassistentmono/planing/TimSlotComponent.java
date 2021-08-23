package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.interfaces.TimeSlotInterface;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import org.springframework.stereotype.Component;

@Component
public class TimSlotComponent {

  public TimeSlot createTimeSlot(TimeSlot newTimeSlot) {
    return newTimeSlot;
  }
}
