package de.mankianer.todoassistentmono.planing.generators;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.planing.DayProfileController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SimpleYearSchemeGenerator implements YearSchemeGenerator {

  private final DayProfileController dayProfileController;

  public SimpleYearSchemeGenerator(
      DayProfileController dayProfileController) {
    this.dayProfileController = dayProfileController;
  }

  @Override
  public YearScheme createYearScheme(int year) {
    int daysOfYear = LocalDateTime.of(year, 12, 31, 12, 0).getDayOfYear();
    log.info("{} hat {} Tage?", year, daysOfYear);
    YearScheme yearScheme = new YearScheme(year);
    LocalDate day = LocalDate.of(year, 1, 1);
    for (int i = 0; i < daysOfYear; i++) {
      DayContext context = new DayContext(day);
      DayProfile dayProfile = dayProfileController.getDayProfileByDate(day);
      yearScheme.getAllDaySchemes().add(dayProfile.planDayScheme(context));
      day = day.plusDays(1);
    }
    yearScheme.setYear(year);
    return yearScheme;
  }

  @Override
  public String getName() {
    return "SimpleYearSchemeGenerator";
  }
}
