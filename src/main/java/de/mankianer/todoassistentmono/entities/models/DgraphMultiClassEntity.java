package de.mankianer.todoassistentmono.entities.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DgraphMultiClassEntity extends DgraphEntity {

  private String multiClassIdentifier = getClass().getSimpleName();

}
