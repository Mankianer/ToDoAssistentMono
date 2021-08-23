package de.mankianer.todoassistentmono.google.services.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import de.mankianer.todoassistentmono.calendar.interfaces.CalendarServiceInterface;
import de.mankianer.todoassistentmono.google.events.GoogleOAuthLoginEvent;
import de.mankianer.todoassistentmono.google.utiles.GoogleCalenderUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CalendarService implements CalendarServiceInterface {

  private NetHttpTransport httpTransport;

  @Value("${calendarID}")
  private String calendarID;
  private Calendar calendar;

  @PostConstruct
  public void init() {
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
  }

  private void loadCollection(@NonNull Credential credential) {
    calendar = new Calendar.Builder(httpTransport, new GsonFactory(),
        credential).setApplicationName("ToDo Assistent").build();
  }

  @Override
  public Event createNewEvent(String titel, String description, LocalDateTime startDate,
      TemporalAmount duration) throws IOException {
    Event event = new Event().setSummary(titel).setDescription(description);

    EventDateTime start = GoogleCalenderUtils.converToEventDate(startDate);
    event.setStart(start);
    EventDateTime end = GoogleCalenderUtils.converToEventDate(startDate.plus(duration));
    event.setEnd(end);

    event = calendar.events().insert(calendarID, event).execute();
    return event;
  }

  @Override
  public Event findByID(String eventID) throws IOException {
    return calendar.events().get(calendarID, eventID).execute();
  }

  @Override
  public Event update(Event event) throws IOException {
    return calendar.events().update(calendarID, event.getId(), event).execute();
  }

  @Override
  public Events getBetween(LocalDateTime from, LocalDateTime to) throws IOException {
    return calendar.events().list(calendarID)
        .setTimeMin(GoogleCalenderUtils.convertToGoogleDateTime(from))
        .setTimeMax(GoogleCalenderUtils.convertToGoogleDateTime(to))
        .setTimeZone(ZoneId.systemDefault().getId()).execute();
  }

  // List the next 10 events from the primary calendar.
  public void printNext10() {
    DateTime now = new DateTime(System.currentTimeMillis());
    Events events = null;
    try {
      events = calendar.events().list(calendarID)
          .setMaxResults(10)
          .setTimeMin(now)
          .setOrderBy("startTime")
          .setSingleEvents(true)
          .execute();
      List<Event> items = events.getItems();
      if (items.isEmpty()) {
        System.out.println("No upcoming events found.");
      } else {
        System.out.println("Upcoming events");
        for (Event event : items) {
          DateTime start = event.getStart().getDateTime();
          if (start == null) {
            start = event.getStart().getDate();
          }
          System.out.printf("%s (%s)\n", event.getSummary(), start);
        }
      }
    } catch (IOException e) {
      log.warn(e);
    }
  }


  @EventListener
  public void onGoogleOAuthLogin(GoogleOAuthLoginEvent googleOAuthLogin) {
    log.info(googleOAuthLogin);
    loadCollection(googleOAuthLogin.getCredential());
  }

}
