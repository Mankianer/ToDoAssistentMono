package de.mankianer.todoassistentmono.dev;

import de.mankianer.todoassistentmono.utils.ToDoAssistentDgraphClientBean;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin
@RestController()
@RequestMapping("/dev/")
public class DevRestController {

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private ToDoAssistentDgraphClientBean dgraph;

  @PostConstruct
  public void init(){
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  @GetMapping("dgraph/v")
  public ResponseEntity<?> dgraphVersion() {
    return new ResponseEntity(dgraph.getDgraphClient().checkVersion().getTag(), HttpStatus.OK);
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
