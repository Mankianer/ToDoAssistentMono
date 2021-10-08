package de.mankianer.todoassistentmono.planing;

import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ValueException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
  public Set<Entry<String, Map<String, ParameterType>>> getRegisteredCondition() {
    return dayProfileConditionController.getRegisteredConditionParameterMap();
  }

  @GetMapping("/{uid}")
  public DayProfileCondition getRegisteredCondition(@PathVariable String uid) {
    return dayProfileConditionController.getRepo().findByUid(uid);
  }

  @PostMapping("/create/{identifier}")
  public ResponseEntity createNewCondition(@PathVariable String identifier, @RequestBody Map<String, ?> values, @RequestParam(defaultValue = "false") boolean dryRun) {
    try {
      DayProfileCondition newCondition = dayProfileConditionController.createNewCondition(
          identifier, values);
      if(!dryRun){
        newCondition = dayProfileConditionController.getRepo().saveToDGraph(newCondition);
      }
      return ResponseEntity.ok(newCondition);
    } catch (ValueException e) {
      log.warn("Wrong Value",e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
