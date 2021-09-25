package de.mankianer.todoassistentmono.entities.models.planing;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearSchemeGeneratorConfig extends DgraphEntity {

  private List<DayProfileRule> dayProfileRules;
  private DayProfile defaultDayProfile;
}
