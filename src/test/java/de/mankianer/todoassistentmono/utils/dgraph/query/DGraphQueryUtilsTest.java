package de.mankianer.todoassistentmono.utils.dgraph.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DGraphQueryUtilsTest {

  @Test
  public void createFindByValueQuery() throws NoSuchFieldException {
    String findByValueQuery = """
        query findByValue($year: int ) {
        findByValue (func: eq(year, $year)) {
        uid
        year
        allDaySchemes
        {
        uid
        timeSlots
        {
        uid
        calenderEntry
        name
        start
        toDoFilter
        {
        uid
        }
        end
        }
        usedContext
        {
        date
        uid
        }
        }
        }
        }""";
    assertEquals(findByValueQuery,
        DGraphQueryUtils.createFindByValueQuery("year", "year", DGraphType.INT,
                DGraphQueryUtils.getFieldMap(YearScheme.class, Map.of(), ""))
            .buildQueryString());
  }

  @Test
  public void findDGraphType() {
    assertEquals(DGraphType.INT, DGraphQueryUtils.findDGraphType(Integer.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtils.findDGraphType(Float.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtils.findDGraphType(Double.class));
    assertEquals(DGraphType.BOOLEAN, DGraphQueryUtils.findDGraphType(Boolean.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtils.findDGraphType(LocalDateTime.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtils.findDGraphType(LocalDate.class));
    assertEquals(DGraphType.DEFAULT, DGraphQueryUtils.findDGraphType(getClass()));
  }

  @Test
  void createQueryString() {
    assertEquals("""
        query queryName($uid: string) {
         functionName(func: uid($uid)) {
        fieldsPart  }
        }""", DGraphQueryUtils.createQueryString("fieldsPart", "queryName", " functionName"));
  }

  @Test
  void convertQueryMapToField() {
    assertEquals("""
        uid
        year
        allDaySchemes
        {
        uid
        timeSlots
        {
        uid
        calenderEntry
        name
        start
        toDoFilter
        {
        uid
        }
        end
        }
        usedContext
        {
        date
        uid
        }
        }
        """, DGraphQueryUtils.convertQueryMapToField(
        DGraphQueryUtils.getFieldMap(YearScheme.class, Map.of(), "")));
  }

  @Test
  void convertFieldToClass() throws NoSuchFieldException {
    assertEquals(Integer.class,
        DGraphQueryUtils.convertFieldToClass(YearScheme.class.getDeclaredField("year")));
    assertEquals(DayScheme.class,
        DGraphQueryUtils.convertFieldToClass(YearScheme.class.getDeclaredField("allDaySchemes")));
    assertEquals(boolean.class,
        DGraphQueryUtils.convertFieldToClass(YearScheme.class.getDeclaredField("overwriteFlag")));
  }
}
