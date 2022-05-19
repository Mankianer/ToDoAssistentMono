package de.mankianer.todoassistentmono.utils.dgraph.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class DgraphProperty<T extends DgraphEntity> extends DgraphEntity {

  @NonNull
  private String _ConfigName;
  @NonNull
  private String propertyName;
  private T value;
}
