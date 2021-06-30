package de.mankianer.todoassistentmono;

import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController()
@RequestMapping("/dev/")
public class DevRestController {

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @PostConstruct
  public void init(){
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  @GetMapping("hallo")
  public String hallo(){
    return "Hallo \uD83D\uDC4B";
  }

  @PostMapping("password")
  public String hashPassword(@RequestBody String password){
    String encode = bCryptPasswordEncoder.encode(password);
    boolean matches = bCryptPasswordEncoder.matches(password, encode);
    return encode + " #" + matches;
  }
}
