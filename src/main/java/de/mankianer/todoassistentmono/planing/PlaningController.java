package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.utils.google.services.calendar.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaningController {

  private final GoogleCalendarService googleCalendarService;

  public PlaningController(GoogleCalendarService googleCalendarService) {
    this.googleCalendarService = googleCalendarService;
  }
}
