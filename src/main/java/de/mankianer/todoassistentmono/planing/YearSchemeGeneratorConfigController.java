package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.YearSchemeGeneratorConfig;
import de.mankianer.todoassistentmono.repos.YearSchemeGeneratorConfigRepo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class YearSchemeGeneratorConfigController {

  private final YearSchemeGeneratorConfigRepo yearSchemeGeneratorConfigRepo;

  private Map<String, YearSchemeGeneratorConfig> loadedConfigMap;

  public YearSchemeGeneratorConfigController(
      YearSchemeGeneratorConfigRepo yearSchemeGeneratorConfigRepo) {
    this.yearSchemeGeneratorConfigRepo = yearSchemeGeneratorConfigRepo;
    loadedConfigMap = new HashMap<>();
  }

  public YearSchemeGeneratorConfig getConfigByTitle(String title) {
    return loadedConfigMap.get(title);
  }

  public YearSchemeGeneratorConfig saveToDB(YearSchemeGeneratorConfig newConfig) {
    newConfig = yearSchemeGeneratorConfigRepo.saveToDGraph(
        newConfig);

    loadedConfigMap.put(newConfig.getTitel(), newConfig);
    //TODO Save in Global Dgraph Config Object

    return newConfig;
  }
}
