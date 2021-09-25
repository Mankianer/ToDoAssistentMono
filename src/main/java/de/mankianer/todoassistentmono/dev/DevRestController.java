package de.mankianer.todoassistentmono.dev;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gson.GsonBuilder;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.planing.generators.SimpleYearSchemeGenerator;
import de.mankianer.todoassistentmono.repos.TimeSlotsRepo;
import de.mankianer.todoassistentmono.utils.dgraph.DgraphService;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import de.mankianer.todoassistentmono.utils.google.models.ClientCredential;
import de.mankianer.todoassistentmono.utils.google.services.GoogleService;
import de.mankianer.todoassistentmono.utils.google.services.calendar.GoogleCalendarService;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController()
@RequestMapping("/dev/")
public class DevRestController {

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private final DgraphService dgraph;

  private final GoogleService googleService;
  private final GoogleCalendarService calendarService;
  private final TimeSlotsRepo timeSlotComponent;

  public DevRestController(DgraphService dgraph,
      GoogleService googleService, GoogleCalendarService calendarService,
      TimeSlotsRepo timeSlotComponent) {
    this.dgraph = dgraph;
    this.googleService = googleService;
    this.calendarService = calendarService;
    this.timeSlotComponent = timeSlotComponent;
  }

  @PostConstruct
  public void init() {
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  @GetMapping("dgraph/v")
  public ResponseEntity<?> dgraphVersion() {
    return new ResponseEntity(dgraph.getDgraphClient().checkVersion().getTag(), HttpStatus.OK);
  }

  @GetMapping("dgraph/save")
  public String dgraphSave() {
    TimeSlot timeSlot = new TimeSlot();
    timeSlot.setName("Name");
    dgraph.saveToDGraph(timeSlot);
    return "ok";
  }

  @GetMapping("dgraph/uid/{uid}/")
  public TimeSlot findByUid(@PathVariable("uid") String uid) {
    var byUid = timeSlotComponent.findByUid(uid);
    return byUid;
  }

  @GetMapping("yearscheme/simple/{year}/")
  public YearScheme getSimpleYearScheme(@PathVariable("year") int year) {
    return new SimpleYearSchemeGenerator().createYearScheme(year);
  }

  @GetMapping(value = "gson/", produces = "application/json")
  public String getGsonTest() {

    return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeTypeAdapter()).create()
              .toJson(new SimpleYearSchemeGenerator().createYearScheme(2021));
  }


  @GetMapping("fields")
  public List<Field> getAllFields() {
    TimeSlot timeSlot = new TimeSlot();
    return timeSlot.getAllFields();
  }

  @GetMapping("islogin")
  public String islogin() {
    return "yes";
  }

  @GetMapping("js")
  public String testScriptEngine() throws ScriptException, NoSuchMethodException {
   ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");

    engine.eval("function composeGreeting(name) {" +
        "return fals" +
        "}");
    Invocable invocable = (Invocable) engine;

    boolean funcResult = (boolean) invocable.invokeFunction("composeGreeting", "marvin");


    return (funcResult ? "ok" : "nee");
  }

  @GetMapping("printCal")
  public String printCal() {
    calendarService.printNext10();
    return "ok";
  }

  @GetMapping("hallo")
  public String hallo(HttpServletResponse response) {
    response.setHeader("Set-Cookie",
        "Authorization=Bearer+eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxOTQwNTIwMzY5LCJpYXQiOjE2MjUxNjAzNjl9.HtDho41fk77VT5228Z64O1HI6oyXVsQwYxSz5d79Wkw; Max-Age=3153600; Path=/; HttpOnly; SameSite=None; Secure;");
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
