package de.mankianer.todoassistentmono.calendar;

import com.github.caldav4j.CalDAVCollection;
import com.github.caldav4j.exceptions.CalDAV4JException;
import de.mankianer.todoassistentmono.entities.events.GoogleOAuthLoginEvent;
import de.mankianer.todoassistentmono.google.GoogleService;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CalendarService {

  private String calenderUri = "https://apidata.googleusercontent.com/caldav/v2/aai3sca7sq847vj610pm73j7qo@group.calendar.google.com/events";

  private CalDAVCollection collection;

  @PostConstruct
  public void init() {
  }

  private void loadCollection(@NonNull String accessToken) {
      this.collection = new CalDAVCollection(calenderUri);
      HttpClient httpclient = HttpClients.custom()
          .addInterceptorLast((HttpRequest request, HttpContext context) -> {
            request.setHeader("Authorization", "Bearer " + accessToken);
          }).build();

//      try {
//        collection.testConnection(httpclient);
//      } catch (CalDAV4JException e) {
//        log.error(e);
//      }
  }

  @EventListener
  public void onGoogleOAuthLogin(GoogleOAuthLoginEvent googleOAuthLogin) {
    log.info(googleOAuthLogin);
    loadCollection(googleOAuthLogin.getNewAccessToken());
  }

}
