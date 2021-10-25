package de.mankianer.todoassistentmono.entities.models;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default YearScheme for access DGraphObject.
 * Starting point save the planing.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class YearScheme extends DgraphEntity {

  private List<DayScheme> allDaySchemes = new ArrayList<>();
  @NonNull
  private Integer year;

  @JsonIgnore
  private boolean overwriteFlag;

}
