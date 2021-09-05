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

  /**
   * Only one YearScheme per Year.
   * Only saves YearScheme if no other or overwriteFlag is set
   * @param entity
   * @return saved entity or if other found other will returned
   */
  @Override
  public YearScheme saveToDGraph(YearScheme entity) {
    YearScheme other = findByYear(entity.getYear());
    if(other != null && entity.isOverwriteFlag()){
      deleteFromDGraphByUid(other.getUid());
      other = null;
    }
    if(other != null) {
      return other;
    }
    return super.saveToDGraph(entity);
  }
}
