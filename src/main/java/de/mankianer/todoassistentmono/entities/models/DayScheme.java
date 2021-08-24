package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.ContextInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DaySchemeInterface;
import de.mankianer.todoassistentmono.entities.interfaces.TimeSlotInterface;
import java.util.List;
import lombok.Data;

@Data
public class DayScheme extends DgraphEntity implements DaySchemeInterface {

  private List<TimeSlotInterface> timeSlots;
  private ContextInterface planedContextInterface;
}
