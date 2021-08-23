package de.mankianer.todoassistentmono.calendar.interfaces;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public interface CalendarServiceInterface {

  Event createNewEvent(String titel, String description, LocalDateTime startDate,
      TemporalAmount duration) throws IOException;

  Event findByID(String eventID) throws IOException;

  Event update(Event event) throws IOException;

  Events getBetween(LocalDateTime from, LocalDateTime to) throws IOException;
}
