package de.mankianer.todoassistentmono.rest;

import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import de.mankianer.todoassistentmono.repos.TimeSlotsRepo;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("timeslot")
@RestController
public class TimeSlotRestController {

  private final TimeSlotsRepo timeSlotsRepo;

  public TimeSlotRestController(TimeSlotsRepo timeSlotsRepo) {
    this.timeSlotsRepo = timeSlotsRepo;
  }

  @GetMapping("/{uid}")
  public TimeSlot findByUid(@PathVariable("uid") String uid) {
    return timeSlotsRepo.findByUid(uid);
  }

  @PostMapping("/")
  public ResponseEntity createNew(@RequestBody TimeSlot timeSlot) {
    if (timeSlot.getUid() != null) {
      return ResponseEntity.badRequest().body("Use PUT to update!");
    }
    return ResponseEntity.ok(timeSlotsRepo.saveToDGraph(timeSlot));
  }

  @PutMapping("/")
  public ResponseEntity update(@RequestBody TimeSlot timeSlot) {
    if (timeSlot.getUid() == null) {
      return ResponseEntity.badRequest().body("Use POST to update!");
    }
    return ResponseEntity.ok(timeSlotsRepo.saveToDGraph(timeSlot));
  }

  @DeleteMapping("/{uid}")
  public boolean delete(@PathVariable("uid") String uid) {
    log.error("Delete is still not Implemented!");
    return false;
  }
}
