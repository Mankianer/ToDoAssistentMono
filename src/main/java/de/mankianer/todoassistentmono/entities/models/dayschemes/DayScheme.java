package de.mankianer.todoassistentmono.entities.models.dayschemes;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default DayProfile for access DGraphObject.
 * It creates a mapping of a day and TimeSlots.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class DayScheme extends DgraphEntity {

  private List<TimeSlot> timeSlots;
  @NonNull
  private DayContext usedContext;
}
