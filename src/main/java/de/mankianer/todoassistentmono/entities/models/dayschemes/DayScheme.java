package de.mankianer.todoassistentmono.entities.models.dayschemes;

import de.mankianer.todoassistentmono.entities.interfaces.ContextInterface;
import de.mankianer.todoassistentmono.entities.interfaces.DaySchemeInterface;
import de.mankianer.todoassistentmono.entities.interfaces.TimeSlotInterface;
import de.mankianer.todoassistentmono.entities.models.Context;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class DayScheme extends DgraphEntity {

  private List<TimeSlot> timeSlots;
  @NonNull
  private Context usedContext;
}
