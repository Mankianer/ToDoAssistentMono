package de.mankianer.todoassistentmono.planing;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition.ParameterType;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.repos.DayProfileConditionRepo;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DayProfileConditionControllerTest {

  @Test
  void getConditionParameterMap() {
    DayProfileConditionRepo dayProfileConditionRepo = Mockito.mock(DayProfileConditionRepo.class);
    DayProfileConditionController dayProfileConditionController = new DayProfileConditionController(dayProfileConditionRepo);
    dayProfileConditionController.register(JSDayProfileCondition.class);
    String s = new Gson().toJson(dayProfileConditionController.getConditionParameterMap());

    assertThat(dayProfileConditionController.getConditionParameterMap()).usingRecursiveComparison()
        .isEqualTo(Map.of("JSDayProfileCondition", Map.of("script",
            ParameterType.STRING)));
  }
}
