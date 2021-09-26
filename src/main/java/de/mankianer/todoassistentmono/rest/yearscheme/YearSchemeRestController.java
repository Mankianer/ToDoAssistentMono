package de.mankianer.todoassistentmono.rest.yearscheme;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.repos.YearSchemeRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/yearscheme")
public class YearSchemeRestController {

  private final YearSchemeRepo yearSchemeRepo;


  public YearSchemeRestController(YearSchemeRepo yearSchemeRepo) {
    this.yearSchemeRepo = yearSchemeRepo;
  }

  @GetMapping("/y{year}/")
  public YearScheme getByYear(@PathVariable("year") int year) {
    return yearSchemeRepo.findByYear(year);
  }

  @GetMapping("/{uid}/")
  public YearScheme getByUid(@PathVariable("uid") String uid) {
    return yearSchemeRepo.findByUid(uid);
  }

  @PostMapping("/")
  public ResponseEntity createNew(@RequestBody YearScheme yearScheme) {
    if (yearScheme.getUid() != null) {
      return ResponseEntity.badRequest().body("Use PUT to update!");
    }
    return ResponseEntity.ok(yearSchemeRepo.saveToDGraph(yearScheme));
  }

  @PutMapping("/")
  public ResponseEntity update(@RequestBody YearScheme yearScheme) {
    if (yearScheme.getUid() == null) {
      return ResponseEntity.badRequest().body("Use POST to create!");
    }
    return ResponseEntity.ok(yearSchemeRepo.saveToDGraph(yearScheme));
  }

  @DeleteMapping("/{uid}/")
  public boolean delete(@PathVariable("uid") String uid) {
    return yearSchemeRepo.deleteFromDGraphByUid(uid);
  }
}
