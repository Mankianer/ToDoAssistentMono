package de.mankianer.todoassistentmono.rest.yearscheme;

import de.mankianer.todoassistentmono.entities.models.planing.YearSchemeGeneratorConfig;
import de.mankianer.todoassistentmono.planing.YearSchemeGeneratorConfigController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yearscheme/generator/config")
public class YearSchemeGeneratorConfigRestController {

  private final YearSchemeGeneratorConfigController yearSchemeGeneratorConfigController;

  public YearSchemeGeneratorConfigRestController(
      YearSchemeGeneratorConfigController yearSchemeGeneratorConfigController) {
    this.yearSchemeGeneratorConfigController = yearSchemeGeneratorConfigController;
  }

  @PostMapping("/{title}")
  public ResponseEntity saveToDB(@PathVariable String title,
      @RequestBody YearSchemeGeneratorConfig newConfig, //TODO Spring Json Parer ändern
      @RequestParam(defaultValue = "false") boolean override) {
    newConfig.setTitel(title);
    YearSchemeGeneratorConfig oldConfig = yearSchemeGeneratorConfigController.getConfigByTitle(
        title);
    if (oldConfig == null || override) {
      //TODO validation von yearSchemeGeneratorConfigController
      newConfig = yearSchemeGeneratorConfigController.saveToDB(
          newConfig);
      return ResponseEntity.ok(newConfig);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(oldConfig);
    }
  }
}
