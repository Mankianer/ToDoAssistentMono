package de.mankianer.todoassistentmono.entities.models;

import de.mankianer.todoassistentmono.entities.interfaces.DayProfileInterface;
import de.mankianer.todoassistentmono.entities.interfaces.YearSchemeInterface;
import de.mankianer.todoassistentmono.entities.models.dayprofiles.DayProfile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class YearScheme extends DgraphEntity {

  private List<DayProfile> allDayProfiles = new ArrayList<>();
  @NonNull
  private int year;

}
