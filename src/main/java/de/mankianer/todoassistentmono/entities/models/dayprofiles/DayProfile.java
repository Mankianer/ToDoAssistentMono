package de.mankianer.todoassistentmono.entities.models.dayprofiles;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default DayProfile for access DGraphObject.
 * Profile of a DaySchema witch is planed with the Context of a Day.
 */
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class DayProfile extends DgraphEntity {

  @NonNull
  private String name;

  private DayScheme planedDayScheme;

  public DayScheme planDayScheme(DayContext context) {
    return null;
  }
}
