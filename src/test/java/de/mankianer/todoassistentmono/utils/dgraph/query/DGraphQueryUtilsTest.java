package de.mankianer.todoassistentmono.utils.dgraph.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQueryFilterFunctionCompare.CompareTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DGraphQueryUtilsTest {

  @Test
  public void createFindByValueQuery() {
    String findByValueQuery = """
        query findFilters($year: int) {
        findFilters (func: eq(year, $year)) {
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
  public void createFindByUidQuery() {
    String findByValueQuery = """
        query findFilters($uid: string) {
        findFilters (func: uid($uid)) {
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
        DGraphQueryUtils.createFindByUidQuery(
                DGraphQueryUtils.getFieldMap(YearScheme.class, Map.of(), ""))
            .buildQueryString());
  }

  @Test
  public void createFindAndFiltersQuery() {
    String findByValueQuery = """
        query findFilters($uid: string, $year: int, $name: string) {
        findFilters (func: uid($uid)) @filter( eq(name, $name) AND gt(year, $year)){
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

    DQueryFilterFunction rootFilter = DGraphQueryUtils.getUidFilterFunction();
    DQueryFilterFunction filter2 = DGraphQueryUtils.getFieldEqualParamFilterFunction("name", "name",
        DGraphType.STRING);
    DQueryFilterFunction filter3 = DGraphQueryUtils.getFieldCompareParamFilterFunction("year",
        "year", DGraphType.INT, CompareTypes.GREATER);
    assertEquals(findByValueQuery,
        DGraphQueryUtils.createFindByAndFilterFunctionsQuery(
                rootFilter, DGraphQueryUtils.getFieldMap(YearScheme.class, Map.of(), ""), filter2,
                filter3)
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
