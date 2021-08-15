package de.mankianer.todoassistentmono.entities.interfaces;

import java.util.List;

public interface DayScheme {

  List<TimeSlot> getTimeSlots();

  Context getPlanedContext();
}
