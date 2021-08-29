package de.mankianer.todoassistentmono.rest.yearscheme;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.planing.generators.SimpleYearSchemeGenerator;
import de.mankianer.todoassistentmono.repos.YearSchemeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yearscheme/generator")
public class YearSchemeGeneratorRestController {

  private SimpleYearSchemeGenerator simpleYearSchemeGenerator;
  private final YearSchemeRepo yearSchemeRepo;

  public YearSchemeGeneratorRestController(YearSchemeRepo yearSchemeRepo) {

    simpleYearSchemeGenerator = new SimpleYearSchemeGenerator();
    this.yearSchemeRepo = yearSchemeRepo;
  }

  @PostMapping("/{year}/save")
  public ResponseEntity createAndSave(@PathVariable("year") int year) {
    YearScheme defaultYearSchema = getDefaultYearSchema(year);
    return ResponseEntity.ok(yearSchemeRepo.saveToDGraph(defaultYearSchema));
  }

  @GetMapping("/{year}/")
  public YearScheme getDefaultYearSchema(@PathVariable("year") int year) {
    return getSimpleYearScheme(year);
  }

  @GetMapping("/simple/{year}/")
  public YearScheme getSimpleYearScheme(@PathVariable("year") int year){
    return simpleYearSchemeGenerator.createYearScheme(year);
  }

}
