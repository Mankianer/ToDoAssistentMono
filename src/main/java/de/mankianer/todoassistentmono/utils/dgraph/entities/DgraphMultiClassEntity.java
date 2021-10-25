package de.mankianer.todoassistentmono.utils.dgraph.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DgraphMultiClassEntity extends DgraphEntity {

  private String multiClassIdentifier = getClass().getSimpleName();

}
