package de.mankianer.todoassistentmono.entities.models.dayprofiles;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default DayProfile for access DGraphObject. Profile of a DaySchema witch is planed with the
 * Context of a Day.
 */
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DayProfile extends DgraphMultiClassEntity {

  @NonNull
  private String name;

  public DayScheme planDayScheme(DayContext context) {
    return null;
  }
}
