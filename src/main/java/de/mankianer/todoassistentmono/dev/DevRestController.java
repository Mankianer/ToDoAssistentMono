package de.mankianer.todoassistentmono.dev;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import de.mankianer.todoassistentmono.google.calendar.CalendarService;
import de.mankianer.todoassistentmono.google.GoogleService;
import de.mankianer.todoassistentmono.google.models.ClientCredential;
import de.mankianer.todoassistentmono.utils.ToDoAssistentDgraphClientBean;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.services.calendar.CalendarScopes;

@Log4j2
@CrossOrigin
@RestController()
@RequestMapping("/dev/")
public class DevRestController {

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private final ToDoAssistentDgraphClientBean dgraph;

  private final GoogleService googleService;
  private final CalendarService calendarService;

  public DevRestController(ToDoAssistentDgraphClientBean dgraph,
      GoogleService googleService, CalendarService calendarService) {
    this.dgraph = dgraph;
    this.googleService = googleService;
    this.calendarService = calendarService;
  }

  @PostConstruct
  public void init() {
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  @GetMapping("dgraph/v")
  public ResponseEntity<?> dgraphVersion() {
    return new ResponseEntity(dgraph.getDgraphClient().checkVersion().getTag(), HttpStatus.OK);
  }

  @GetMapping("islogin")
  public String islogin() {
    return "yes";
  }

  @GetMapping("printCal")
  public String printCal() {
    calendarService.printNext10();
    return "ok";
  }

  @GetMapping("hallo")
  public String hallo(HttpServletResponse response) {
    response.setHeader("Set-Cookie", "Authorization=Bearer+eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxOTQwNTIwMzY5LCJpYXQiOjE2MjUxNjAzNjl9.HtDho41fk77VT5228Z64O1HI6oyXVsQwYxSz5d79Wkw; Max-Age=3153600; Path=/; HttpOnly; SameSite=Lax;");
    return "Hallo \uD83D\uDC4B";
  }

  @PostMapping("password")
  public String hashPassword(@RequestBody String password) {
    String encode = bCryptPasswordEncoder.encode(password);
    boolean matches = bCryptPasswordEncoder.matches(password, encode);
    return encode + " #" + matches;
  }

  @GetMapping("google")
  public String googleTest() {

    AppIdentityCredential credential = new AppIdentityCredential(
        Collections.singletonList(CalendarScopes.CALENDAR));

    ClientCredential clientCredential = googleService.getClientCredential();

    AuthorizationCodeFlow authorizationCodeFlow = new AuthorizationCodeFlow(
        BearerToken.authorizationHeaderAccessMethod(),
        new NetHttpTransport(),
        new GsonFactory(),
        new GenericUrl(clientCredential.getToken_uri()),
        new BasicAuthentication(clientCredential.getClient_id(),
            clientCredential.getClient_secret()),
        clientCredential.getClient_id(),
        clientCredential.getAuth_uri());

//    CalDAVCollection collection = new CalDAVCollection();
//    HttpClient httpclient = HttpClients.createDefault();

    return "token";
  }
}
