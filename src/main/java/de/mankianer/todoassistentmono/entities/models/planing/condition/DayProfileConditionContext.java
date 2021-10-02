package de.mankianer.todoassistentmono.entities.models.planing.condition;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DayProfileConditionContext {
  private LocalDate date;
}
