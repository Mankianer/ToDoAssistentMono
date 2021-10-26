package de.mankianer.todoassistentmono.repos;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import de.mankianer.todoassistentmono.utils.dgraph.services.DgraphService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ContextRepo extends DgraphRepo<DayContext> {

  public ContextRepo(
      DgraphService dgraphClientBean) {
    super(dgraphClientBean.getDgraphClient());
  }
}
