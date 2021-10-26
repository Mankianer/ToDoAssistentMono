package de.mankianer.todoassistentmono.utils.dgraph.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DgraphConfigEntity<T extends DgraphEntity> extends DgraphEntity {

  @NonNull
  private String _ConfigName;
  private List<DgraphProperty<T>> properties;

  public void addProperty(DgraphProperty<T> property) {
    if (properties == null) {
      properties = new ArrayList<>();
    }
    properties.add(property);
  }

}
