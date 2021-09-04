package de.mankianer.todoassistentmono.repos;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphService;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphType;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class YearSchemeRepo extends DgraphRepo<YearScheme> {

  public YearSchemeRepo(
      DgraphService dgraphClientBean) {
    super(dgraphClientBean.getDgraphClient());
  }

  public YearScheme save(YearScheme yearScheme) {

//    yearScheme.getAllDayProfiles().stream().

    yearScheme = super.saveToDGraph(yearScheme);

    return yearScheme;
  }

  @SneakyThrows
  public YearScheme findByYear(int year){
    return findByValue("year", "" + year, DGraphType.STRING);
  }

  @Override
  public YearScheme saveToDGraph(YearScheme entity) {
    return super.saveToDGraph(entity);
  }
}
