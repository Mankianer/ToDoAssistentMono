package de.mankianer.todoassistentmono.utils.dgraph.services;

import de.mankianer.todoassistentmono.utils.dgraph.DgraphConfigRepo;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import org.springframework.stereotype.Service;

/**
 * Service for managing config of the application
 */
@Service
public class DgraphConfigService {

  private final DgraphService dgraphService;

  public DgraphConfigService(
      DgraphService dgraphService) {
    this.dgraphService = dgraphService;
  }

  public <T extends DgraphEntity> T findOrCreate(String configName, String propertyName, T option) {

    return option; //TODO richitg machen!
  }

  private <T extends DgraphEntity> DgraphConfigRepo<T> getNewConfigRepo() {
    return new DgraphConfigRepo<>(dgraphService.getDgraphClient());
  }
}
