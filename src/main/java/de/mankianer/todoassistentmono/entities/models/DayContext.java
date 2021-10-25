package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Default DayContext for access DGraphObject.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class DayContext extends DgraphEntity {

  @NonNull
  private LocalDate date;

}
