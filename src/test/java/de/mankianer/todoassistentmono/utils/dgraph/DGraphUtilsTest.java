package de.mankianer.todoassistentmono.utils.dgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.entities.models.planing.condition.DayProfileCondition;
import de.mankianer.todoassistentmono.entities.models.planing.condition.impl.JSDayProfileCondition;
import de.mankianer.todoassistentmono.planing.DayProfileConditionController;
import de.mankianer.todoassistentmono.repos.DayProfileConditionRepo;
import java.util.Map;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DGraphUtilsTest {

  @Test
  public void findMultiClassParent() {
    assertEquals(DayProfileCondition.class,
        DGraphUtils.findMultiClassParent(JSDayProfileCondition.class));
    assertEquals(DayProfileCondition.class,
        DGraphUtils.findMultiClassParent(DayProfileCondition.class));
    assertNull(DGraphUtils.findMultiClassParent(Object.class));
  }

  @Test
  void parseMultiClassIdentifierFromJson() {
    String json = """
        {
          "multiClassIdentifier": "test"
        }
        """;
    assertEquals("test", DGraphUtils.parseMultiClassIdentifierFromJson(json));
  }

  @Order(-1)
  @Test
  void findMultiClassWithPath() {
    //Unregisterd on DGraphRepo
    Map<String, Class<? extends DgraphMultiClassEntity>> multiClassWithPath = DGraphUtils.findMultiClassWithPath(
        DayProfileCondition.class, "", (path) -> JSDayProfileCondition.class.getSimpleName());
    assertThat(multiClassWithPath).usingRecursiveComparison()
        .isEqualTo(Map.of("", DgraphMultiClassEntity.class));

    //insert not Parent
    multiClassWithPath = DGraphUtils.findMultiClassWithPath(
        JSDayProfileCondition.class, "", (path) -> JSDayProfileCondition.class.getSimpleName());
    assertThat(multiClassWithPath).usingRecursiveComparison()
        .isEqualTo(Map.of("", DgraphMultiClassEntity.class));

    //RegisterDGraph
    DayProfileConditionRepo repo = Mockito.mock(DayProfileConditionRepo.class);
    DgraphRepo.registerMultiCastEntityResolver(DayProfileCondition.class,
        (String identifier) -> Map.of(JSDayProfileCondition.class.getSimpleName(),
            JSDayProfileCondition.class).get(identifier), new DayProfileConditionController(repo));

    multiClassWithPath = DGraphUtils.findMultiClassWithPath(
        DayProfileCondition.class, "", (path) -> JSDayProfileCondition.class.getSimpleName());
    assertThat(multiClassWithPath).usingRecursiveComparison()
        .isEqualTo(Map.of("", JSDayProfileCondition.class));

    //insert not Parent
    multiClassWithPath = DGraphUtils.findMultiClassWithPath(
        JSDayProfileCondition.class, "", (path) -> JSDayProfileCondition.class.getSimpleName());
    assertThat(multiClassWithPath).usingRecursiveComparison()
        .isEqualTo(Map.of("", JSDayProfileCondition.class));
  }

  @Test
  void blankInstandOfMultiClassParent() {
    assertEquals(new DgraphMultiClassEntity(),
        DGraphUtils.blankInstandOfMultiClassParent(DayProfileCondition.class));
    assertEquals(new JSDayProfileCondition(),
        DGraphUtils.blankInstandOfMultiClassParent(JSDayProfileCondition.class));
  }
}
