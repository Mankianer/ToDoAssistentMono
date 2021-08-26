package de.mankianer.todoassistentmono.planing.generators;

import de.mankianer.todoassistentmono.entities.models.Context;
import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.SimpleDayProfile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SimpleYearSchemeGenerator {

  public YearScheme createYearScheme(int year){
    int daysOfYear = LocalDateTime.of(year, 12, 31, 12, 0).getDayOfYear();
    log.info("{} hat {} Tage?", year, daysOfYear);
    YearScheme yearScheme = new YearScheme();
    LocalDate day = LocalDate.of(year, 1, 1);
    for (int i = 0; i < daysOfYear; i++) {
      Context context = new Context(day);
      SimpleDayProfile dayProfile = new SimpleDayProfile();
      yearScheme.getAllDayProfiles().add(dayProfile);
      dayProfile.setPlanedDayScheme(dayProfile.planDayScheme(context));
      day = day.plusDays(1);
    }
    return yearScheme;
  }
}
