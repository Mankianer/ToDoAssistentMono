package de.mankianer.todoassistentmono.utils.dgraph.query;

import static org.junit.jupiter.api.Assertions.*;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DGraphQueryUtilsTest {

  @Test
  public void testCreateFindByValueQuery() throws NoSuchFieldException {
    String findByValueQuery = """
        query findByValue($year: INT ) {
        findByValue (func: eq(year, $year)) {
        uid
        year
        allDayProfiles
        {
        uid
        planedDayScheme
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
        name
        }      
        }
        }""";
    assertEquals(findByValueQuery,
        DGraphQueryUtils.createFindByValueQuery("year", "year", YearScheme.class)
            .buildQueryString());
  }

  @Test
  public void testFindDGraphType() {
    assertEquals(DGraphType.INT, DGraphQueryUtils.findDGraphType(Integer.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtils.findDGraphType(Float.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtils.findDGraphType(Double.class));
    assertEquals(DGraphType.BOOLEAN, DGraphQueryUtils.findDGraphType(Boolean.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtils.findDGraphType(LocalDateTime.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtils.findDGraphType(LocalDate.class));
    assertEquals(DGraphType.DEFAULT, DGraphQueryUtils.findDGraphType(getClass()));
  }

}
