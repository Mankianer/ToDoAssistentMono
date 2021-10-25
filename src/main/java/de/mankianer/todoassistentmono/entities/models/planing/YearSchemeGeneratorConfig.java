package de.mankianer.todoassistentmono.entities.models.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Default YearSchemeGeneratorConfig for access DGraphObject.
 * Listing of DayProfileRules and a default DayProfile in case of negation of all rules.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class YearSchemeGeneratorConfig extends DgraphEntity {

  private String titel;
  private List<DayProfileRule> dayProfileRules;
  private DayProfile defaultDayProfile;
}
