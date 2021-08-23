package de.mankianer.todoassistentmono.google.utiles;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class GoogleCalenderUtils {
  static public Date convertToDateViaInstant(LocalDateTime dateToConvert) {
    return java.util.Date
        .from(dateToConvert.atZone(ZoneId.systemDefault())
            .toInstant());
  }

  static public DateTime convertToGoogleDateTime(LocalDateTime localDateTime) {
    return new DateTime(convertToDateViaInstant(localDateTime));
  }

  static public EventDateTime converToEventDate(LocalDateTime localDateTime) {
    return new EventDateTime()
        .setDateTime(new DateTime(GoogleCalenderUtils.convertToDateViaInstant(localDateTime)))
        .setTimeZone(ZoneId.systemDefault().getId());
  }
}
