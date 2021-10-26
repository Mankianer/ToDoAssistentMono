package de.mankianer.todoassistentmono.utils.dgraph;

import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphConfigEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphProperty;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphType;
import io.dgraph.DgraphClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;

public class DgraphConfigRepo<T extends DgraphEntity> extends DgraphRepo<DgraphConfigEntity<T>> {

  public DgraphConfigRepo(DgraphClient dgraphClient) {
    super(dgraphClient);
  }

  @SneakyThrows(NoSuchFieldException.class)
  public Optional<DgraphConfigEntity<T>> findConfig(String configName) {
    DgraphConfigEntity<T> config = findByValue("_ConfigName", configName,
        DGraphType.STRING);
    return Optional.of(config);
  }

  public Optional<DgraphConfigEntity<T>> saveNewConfig(String configName,
      DgraphProperty<T>... properties) {
    return Optional.of(saveToDGraph(createNewConfig(configName, properties)));
  }

  public Optional<DgraphConfigEntity<T>> saveProperty(String configName, String propertyName,
      T value) {
    DgraphProperty<T> newProperty = new DgraphProperty<>(propertyName, value);
    DgraphConfigEntity<T> editedConfig = findConfig(configName).map(
        config -> {
          config.addProperty(newProperty);
          return config;
        }).orElse(
        createNewConfig(configName, newProperty));
    return Optional.of(saveToDGraph(editedConfig));
  }

  private DgraphConfigEntity<T> createNewConfig(String configName,
      DgraphProperty<T>... properties) {
    DgraphConfigEntity<T> newConfig = new DgraphConfigEntity<>(configName);
    ArrayList<DgraphProperty<T>> list = new ArrayList<>(List.of(properties));
    newConfig.setProperties(list);
    return newConfig;
  }
}
