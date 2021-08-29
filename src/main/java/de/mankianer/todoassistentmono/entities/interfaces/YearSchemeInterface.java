package de.mankianer.todoassistentmono.entities.interfaces;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface YearSchemeInterface {

  List<DayProfile> getAllDayProfiles();

  int getYear();

}
