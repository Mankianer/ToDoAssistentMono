package de.mankianer.todoassistentmono.entities.interfaces;

import java.util.List;

public interface DaySchemeInterface {

  List<TimeSlotInterface> getTimeSlots();

  ContextInterface getPlanedContext();
}
