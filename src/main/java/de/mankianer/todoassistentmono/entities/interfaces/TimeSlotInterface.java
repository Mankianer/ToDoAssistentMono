package de.mankianer.todoassistentmono.entities.interfaces;

import java.time.LocalDateTime;

public interface TimeSlotInterface {

  CalendarEntry getCalenderEntry();

  ToDoFilter getToDoFilter();

  String getName();

  LocalDateTime getStart();

  LocalDateTime getEnd();

  TimeSlotInterface getNext();

  TimeSlotInterface getPrevious();
}
