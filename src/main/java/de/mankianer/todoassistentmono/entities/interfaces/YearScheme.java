package de.mankianer.todoassistentmono.entities.interfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface YearScheme {

  DayProfile getDayProfile(LocalDateTime dateTime);

  List<DayProfile> getAllDayProfiles();

}
