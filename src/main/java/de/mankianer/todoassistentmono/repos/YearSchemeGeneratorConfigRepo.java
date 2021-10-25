package de.mankianer.todoassistentmono.repos;

import de.mankianer.todoassistentmono.entities.models.planing.YearSchemeGeneratorConfig;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphService;
import org.springframework.stereotype.Component;

@Component
public class YearSchemeGeneratorConfigRepo extends DgraphRepo<YearSchemeGeneratorConfig> {

  public YearSchemeGeneratorConfigRepo(DgraphService dgraphClientBean) {
    super(dgraphClientBean.getDgraphClient());
  }
}
