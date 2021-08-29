package de.mankianer.todoassistentmono.entities.models.dayprofiles;

import de.mankianer.todoassistentmono.entities.interfaces.ContextInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DayProfileInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DaySchemeInterface;
import de.mankianer.todoassistentmono.entities.models.Context;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class DayProfile extends DgraphEntity {

  @NonNull
  private String name;

  private DayScheme planedDayScheme;

  public DayScheme planDayScheme(Context context) {
    return null;
  }
}
