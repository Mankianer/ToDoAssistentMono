package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtils;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQuery;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DGraphUtils {

  public static String removeFunctionNameFromJson(String json, DQuery query){
    return json.substring(
        ("{\"" + query.getFunction().getFunctionName() + "\":").length(),
        json.length() - 1);
  }

  @Deprecated
  public static String parseMultiClassIdentifierFromJson(String json) {
    JsonReader jsonReader = new Gson().newJsonReader(new StringReader(json));
    String identifier = null;
    while (true) {
      try {
        if (!jsonReader.hasNext()) {
          break;
        }
        JsonToken token = jsonReader.peek();
        if (JsonToken.NAME.equals(token)) {
          if (jsonReader.nextName().equalsIgnoreCase("multiClassIdentifier")) {
            JsonToken possibleIdentifier = jsonReader.peek();
            if (JsonToken.STRING.equals(possibleIdentifier)) {
              identifier = jsonReader.nextString();
              break;
            }
          }
        }

        if (JsonToken.BEGIN_ARRAY.equals(token)) {
          jsonReader.beginArray();
        } else if (JsonToken.END_ARRAY.equals(token)) {
          jsonReader.endArray();
        } else if (JsonToken.BEGIN_OBJECT.equals(token)) {
          jsonReader.beginObject();
        } else if (JsonToken.END_OBJECT.equals(token)) {
          jsonReader.endObject();
        } else {
          jsonReader.skipValue();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return identifier;
  }

}
