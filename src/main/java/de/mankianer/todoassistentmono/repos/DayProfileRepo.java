package de.mankianer.todoassistentmono.repos;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import de.mankianer.todoassistentmono.utils.dgraph.services.DgraphService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileRepo extends DgraphRepo<DayProfile> {

  public DayProfileRepo(
      DgraphService dgraphClientBean) {
    super(dgraphClientBean.getDgraphClient());
  }
}
