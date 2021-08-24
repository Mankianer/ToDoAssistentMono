package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.DayProfileInterface;
import de.mankianer.todoassistentmono.entities.interfaces.YearSchemeInterface;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class YearScheme extends DgraphEntity implements YearSchemeInterface {
  private List<DayProfileInterface> allDayProfiles;
}
