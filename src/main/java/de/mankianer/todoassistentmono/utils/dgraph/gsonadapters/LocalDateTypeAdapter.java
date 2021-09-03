package de.mankianer.todoassistentmono.utils.dgraph.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

  @Override
  public void write(JsonWriter out, LocalDate value) throws IOException {
    if (value != null) {
      String format = value.format(DateTimeFormatter.ISO_LOCAL_DATE);
      out.value(format);
    } else {
      out.nullValue();
    }
  }

  @Override
  public LocalDate read(JsonReader in) throws IOException {
    LocalDate dateTime = null;
    if (in.hasNext()) {
      JsonToken peek = in.peek();
      if (peek.equals(JsonToken.STRING)) {
        dateTime = LocalDate.from(
            DateTimeFormatter.ISO_LOCAL_DATE.parse(in.nextString()));
      } else {
        System.out.println("Json Token Type MissMatsch with Date: " + new Gson().toJson(in.peek()));
      }
    }
    return dateTime;
  }
}
