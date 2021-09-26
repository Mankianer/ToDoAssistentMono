package de.mankianer.todoassistentmono.entities.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Default DayContext for access DGraphObject.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayContext extends DgraphEntity {

  @NonNull
  private LocalDate date;

}
