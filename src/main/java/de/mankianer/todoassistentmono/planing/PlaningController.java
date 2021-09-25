package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.planing.generators.SimpleYearSchemeGenerator;
import de.mankianer.todoassistentmono.planing.generators.YearSchemeGenerator;
import de.mankianer.todoassistentmono.utils.google.services.calendar.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaningController {

  private final GoogleCalendarService googleCalendarService;
  private YearSchemeGenerator simpleYearSchemeGenerator;

  public PlaningController(GoogleCalendarService googleCalendarService) {
    this.googleCalendarService = googleCalendarService;

  }

  public String getCurrentYearSchemeGeneratorName(){
    return simpleYearSchemeGenerator.getName();
  }
}
