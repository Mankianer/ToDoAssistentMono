package de.mankianer.todoassistentmono.entities.models.dayschemes;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper=false)
public class DayScheme extends DgraphEntity {

  private List<TimeSlot> timeSlots;
  @NonNull
  private DayContext usedContext;
}
