package de.mankianer.todoassistentmono.entities.interfaces;

import java.util.List;

public interface DayScheme {

  List<TimeSlotInterface> getTimeSlots();

  Context getPlanedContext();
}
