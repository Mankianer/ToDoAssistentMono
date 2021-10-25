package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.SimpleDayProfile;
import de.mankianer.todoassistentmono.repos.DayProfileRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphMultiClassEntityController;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileController extends DgraphMultiClassEntityController<DayProfile> {


  public DayProfileController(DayProfileRepo repo) {
    super(repo);
  }

  @PostConstruct
  public void init() {
    register(SimpleDayProfile.class);
  }

  public DayProfile getDayProfileByDate(LocalDate date) {
    return new SimpleDayProfile();
  }

  public void createNewDayProfile() {

  }
}
