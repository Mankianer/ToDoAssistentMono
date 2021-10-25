package de.mankianer.todoassistentmono.planing;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.SimpleDayProfile;
import de.mankianer.todoassistentmono.repos.DayProfileRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphRepo;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DayProfileController implements JsonDeserializer<DayProfile> {

  @Getter
  private final DayProfileRepo repo;
  private Map<String, Class<? extends DayProfile>> dayProfileMap;

  public DayProfileController(DayProfileRepo repo) {
    DgraphRepo.registerMultiCastEntityResolver(DayProfile.class, this::resolve, this);
    dayProfileMap = new HashMap<>();
    this.repo = repo;
  }

  @PostConstruct
  public void init() {
    register(SimpleDayProfile.class);
  }

  public void register(Class<? extends DayProfile>... dayProfileClasses) {
    for (Class<? extends DayProfile> dayProfileClass : dayProfileClasses) {
      try {
        DayProfile dayProfile = dayProfileClass.getDeclaredConstructor()
            .newInstance();
        dayProfileMap.put(dayProfile.getMultiClassIdentifier(), dayProfileClass);
      } catch (Exception e) {
        log.error(e);
      }
    }
  }

  public Class<? extends DayProfile> resolve(String identifier) {
    return dayProfileMap.get(identifier);
  }

  @Override
  public DayProfile deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    String multiClassIdentifier = json.getAsJsonObject().entrySet().stream()
        .filter(e -> e.getKey().equalsIgnoreCase("multiClassIdentifier"))
        .map(e -> e.getValue().getAsString()).findFirst().orElse(null);
    Class<? extends DayProfile> resolvedClass = resolve(multiClassIdentifier);
    if (resolvedClass != null) {
      DayProfile dayProfile = context.deserialize(json, resolvedClass);
      return dayProfile;
    }
    return null;
  }

  public DayProfile getDayProfileByDate(LocalDate date) {
    return new SimpleDayProfile();
  }

  public void createNewDayProfile() {

  }
}
