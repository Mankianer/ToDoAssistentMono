package de.mankianer.todoassistentmono.utils.dgraph;

import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphConfig;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphProperty;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphType;
import io.dgraph.DgraphClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;

public class DgraphConfigRepo<T extends DgraphEntity> extends DgraphRepo<DgraphProperty<T>> {

  public DgraphConfigRepo(DgraphClient dgraphClient) {
    super(dgraphClient);
  }

  @SneakyThrows(NoSuchFieldException.class)
  public Optional<DgraphConfig<T>> findConfig(String configName) {
    return Optional.of(null);
  }

  public Optional<DgraphConfig<T>> saveNewConfig(String configName,
      DgraphProperty<T>... properties) {
    return Optional.of(saveToDGraph(createNewConfig(configName, properties)));
  }

  public Optional<DgraphProperty<T>> saveProperty(String configName, String propertyName,
      T value) {
    DgraphProperty<T> newProperty = new DgraphProperty<>(configName, propertyName, value);
    return Optional.of(saveToDGraph(newProperty));
  }

}
