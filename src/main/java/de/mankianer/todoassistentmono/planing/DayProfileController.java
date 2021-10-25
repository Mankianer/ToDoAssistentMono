package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.SimpleDayProfile;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DayProfileController {

  public DayProfile getDayProfileByDate(LocalDate date) {
    return new SimpleDayProfile();
  }
}
