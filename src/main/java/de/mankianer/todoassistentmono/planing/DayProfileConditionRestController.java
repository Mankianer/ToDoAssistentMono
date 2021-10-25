package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ValueException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("dayprofilecondition")
public class DayProfileConditionRestController {

  private final DayProfileConditionController dayProfileConditionController;

  public DayProfileConditionRestController(
      DayProfileConditionController dayProfileConditionController) {
    this.dayProfileConditionController = dayProfileConditionController;
  }

  @GetMapping("/registered")
  public Set<?> getRegisteredCondition() {
    return dayProfileConditionController.getRegisteredConditionParameterMap().stream().map(
            e -> e.getValue().entrySet().stream().map(e2 -> {
              SimpleEntry<String, ?> stringListSimpleEntry;
              if (e2.getValue()
                  .getAllowedValues() != null) {
                return new SimpleEntry<>(e.getKey(),
                    new SimpleEntry(e2.getKey(),
                        new SimpleEntry<>(e2.getValue(), e2.getValue().getAllowedValues())));
              }
              return new SimpleEntry<>(e.getKey(), e2);
            }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())))
        .collect(Collectors.toSet());
  }

  @GetMapping("/{uid}")
  public DayProfileCondition getRegisteredCondition(@PathVariable String uid) {
    return dayProfileConditionController.getRepo().findByUid(uid);
  }

  @PostMapping("/create/{identifier}")
  public ResponseEntity createNewCondition(@PathVariable String identifier,
      @RequestBody Map<String, ?> values, @RequestParam(defaultValue = "false") boolean saveToDB,
      @RequestParam(defaultValue = "New Condition") String name) {
    try {
      DayProfileCondition newCondition = dayProfileConditionController.createNewCondition(name,
          identifier, values);
      if (saveToDB) {
        newCondition = dayProfileConditionController.getRepo().saveToDGraph(newCondition);
      }
      return ResponseEntity.ok(newCondition);
    } catch (ValueException e) {
      log.warn("Wrong Value", e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
