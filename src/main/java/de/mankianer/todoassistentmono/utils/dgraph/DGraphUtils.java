package de.mankianer.todoassistentmono.utils.dgraph;

import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;

public class DGraphUtils {

  public static Class<? extends DgraphMultiClassEntity> findMultiClassParent(
      Class clazz) {
    Class<?> possibleParent = clazz;
    while (possibleParent.getSuperclass() != null) {
      if (possibleParent.getSuperclass().equals(DgraphMultiClassEntity.class)) {
        return (Class<? extends DgraphMultiClassEntity>) possibleParent;
      }
      possibleParent = possibleParent.getSuperclass();
    }
    return null;
  }

}
