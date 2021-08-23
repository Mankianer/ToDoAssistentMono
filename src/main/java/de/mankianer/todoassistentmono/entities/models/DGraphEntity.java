package de.mankianer.todoassistentmono.entities.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class DGraphEntity {

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
    while (!superclass.getName()
        .equals("de.mankianer.todoassistentmono.entities.models.DGraphEntity"));

    return declaredFields;
  }

}