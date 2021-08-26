package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.ContextInterface;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Context extends DgraphEntity implements ContextInterface {

  @NonNull
  private LocalDate date;

}
