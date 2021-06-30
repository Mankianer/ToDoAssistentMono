package de.mankianer.todoassistentmono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/dev/")
public class DevRestController {

  @GetMapping("hallo")
  public String hallo(){
    return "Hallo \uD83D\uDC4B";
  }


}
