package de.mankianer.todoassistentmono.utils.dgraph.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Default DgraphEntity for access DGraphObject.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DgraphEntity {

  private String uid;

  @JsonIgnore
  public List<Field> getAllFields() {
    List<Field> declaredFields = new ArrayList<>();
    declaredFields.addAll(List.of(getClass().getDeclaredFields()));
    Class<?> superclass = getClass();
    do {
      superclass = superclass.getSuperclass();
      declaredFields.addAll(List.of(superclass.getDeclaredFields()));
    }
    while (!DgraphEntity.class.getName().equals(superclass.getName()));

    return declaredFields;
  }

}
