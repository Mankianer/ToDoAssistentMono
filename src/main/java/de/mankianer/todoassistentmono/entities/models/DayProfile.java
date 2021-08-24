package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.ContextInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DayProfileInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DaySchemeInterface;
import lombok.Data;

@Data
public class DayProfile extends DgraphEntity implements DayProfileInterface {

  @Override
  public DaySchemeInterface planDayScheme(ContextInterface contextInterface) {
    return null;
  }
}
