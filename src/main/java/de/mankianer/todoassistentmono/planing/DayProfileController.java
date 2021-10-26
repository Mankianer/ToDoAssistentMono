package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.SimpleDayProfile;
import de.mankianer.todoassistentmono.repos.DayProfileRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphMultiClassEntityController;
import de.mankianer.todoassistentmono.utils.dgraph.services.DgraphConfigService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileController extends DgraphMultiClassEntityController<DayProfile> {

  private final DgraphConfigService dgraphConfigService;
  private Map<String, DayProfile> loadedDayProfiles;

  public DayProfileController(DayProfileRepo repo, DgraphConfigService dgraphConfigService) {
    super(repo);
    this.dgraphConfigService = dgraphConfigService;
    loadedDayProfiles = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    register(SimpleDayProfile.class);
  }

  @Override
  protected void onRegister(DayProfile instance, Class clazz) {
    instance = dgraphConfigService.findOrCreate("dayProfileMap", instance.getName(), instance);
    loadedDayProfiles.put(instance.getName(), instance);
  }

  public Map<String, DayProfile> getDayProfileNameDgraphMap() {
    return loadedDayProfiles;
  }

  public DayProfile getDayProfileByDate(LocalDate date) {
    return new SimpleDayProfile(); //TODO richtigen DayProfile zurück geben
  }

  public void createNewDayProfile() {

  }
}
