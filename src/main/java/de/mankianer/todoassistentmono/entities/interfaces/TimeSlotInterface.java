package de.mankianer.todoassistentmono.entities.interfaces;

import java.time.LocalDateTime;

public interface TimeSlotInterface {

  CalendarEntryInterface getCalenderEntry();

  ToDoFilterInterface getToDoFilter();

  String getName();

  LocalDateTime getStart();

  LocalDateTime getEnd();

  TimeSlotInterface getNext();

  TimeSlotInterface getPrevious();
}
