package de.mankianer.todoassistentmono.utils.dgraph.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

  @Override
  public void write(JsonWriter out, LocalDateTime value) throws IOException {
    if (value != null) {
      String format = value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      out.value(format);
    } else {
      out.nullValue();
    }
  }

  @Override
  public LocalDateTime read(JsonReader in) throws IOException {
    LocalDateTime dateTime = null;
    if (in.hasNext()) {
      JsonToken peek = in.peek();
      if (peek.equals(JsonToken.STRING)) {
        String dateString = in.nextString();
        if (dateString.charAt(dateString.length() - 1) == 'Z') {
          dateString = dateString.substring(0, dateString.length() - 1);
        }
        dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      } else {
        System.out.println("Json Token Type MissMatsch with Date: " + new Gson().toJson(in.peek()));
      }
    }
    return dateTime;
  }
}
