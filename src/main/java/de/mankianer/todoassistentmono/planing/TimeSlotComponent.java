package de.mankianer.todoassistentmono.planing;

import com.google.gson.Gson;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import de.mankianer.todoassistentmono.utils.ToDoAssistentDgraphClientBean;
import io.dgraph.DgraphProto.Response;
import java.util.Collections;
import java.util.Map;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class TimeSlotComponent {

  private final ToDoAssistentDgraphClientBean dgraphClientBean;
  private final Gson gson;

  public TimeSlotComponent(
      ToDoAssistentDgraphClientBean dgraphClientBean) {
    this.dgraphClientBean = dgraphClientBean;
    gson = new Gson();
  }

  public TimeSlot saveTimeSlot(TimeSlot newTimeSlot) {
    return dgraphClientBean.saveToDGraph(newTimeSlot);
  }

  public TimeSlot[] findByUid(String uid) {
    return dgraphClientBean.findByUid(uid, TimeSlot.class);
  }
}
