package de.mankianer.todoassistentmono.utils.dgraph.query;

import static org.junit.jupiter.api.Assertions.*;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DGraphQueryUtilesTest {

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
        DGraphQueryUtiles.createFindByValueQuery("year", "year", YearScheme.class)
            .buildQueryString());
  }

  @Test
  public void testFindDGraphType() {
    assertEquals(DGraphType.INT, DGraphQueryUtiles.findDGraphType(Integer.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtiles.findDGraphType(Float.class));
    assertEquals(DGraphType.FLOAT, DGraphQueryUtiles.findDGraphType(Double.class));
    assertEquals(DGraphType.BOOLEAN, DGraphQueryUtiles.findDGraphType(Boolean.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtiles.findDGraphType(LocalDateTime.class));
    assertEquals(DGraphType.DATETIME, DGraphQueryUtiles.findDGraphType(LocalDate.class));
    assertEquals(DGraphType.DEFAULT, DGraphQueryUtiles.findDGraphType(getClass()));
  }

}
