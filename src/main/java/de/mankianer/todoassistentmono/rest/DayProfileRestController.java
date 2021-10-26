package de.mankianer.todoassistentmono.rest;

import de.mankianer.todoassistentmono.planing.DayProfileController;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dayProfile")
public class DayProfileRestController {

  private final DayProfileController dayProfileController;


  public DayProfileRestController(DayProfileController dayProfileController) {
    this.dayProfileController = dayProfileController;
  }

  @GetMapping("/allNames")
  public Set<String> getAllDayProfilesNameList() {
    return dayProfileController.getDayProfileNameDgraphMap().keySet();
  }
}
