package de.mankianer.todoassistentmono.entities.models.dayprofiles;

import de.mankianer.todoassistentmono.entities.models.DayContext;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import de.mankianer.todoassistentmono.entities.models.ToDoFilter;
import de.mankianer.todoassistentmono.entities.models.dayschemes.DayScheme;
import java.time.LocalDate;
import java.util.List;

/**
 * A DayProfile for dev purpose.
 */
public class SimpleDayProfile extends DayProfile {

  public SimpleDayProfile(){
    super("SimpleDayProfile");
  }

  public DayScheme planDayScheme(DayContext context) {

    DayScheme defaultDayScheme = getDefaultDayScheme(context);
    return defaultDayScheme;
  }

  /**
   *
   * @param date - target Date
   * @return DayScheme with day(6:00-18:00) and nighe(18:00-6:00+1d)
   */
  private DayScheme getDefaultDayScheme(DayContext context) {
    DayScheme dayScheme = new DayScheme(context);
    LocalDate date = context.getDate();
    TimeSlot day = new TimeSlot("day",new ToDoFilter(),  date.atTime(6, 0) );
    TimeSlot night = new TimeSlot("night",new ToDoFilter(), date.atTime(18, 0));
    night.setEnd(date.plusDays(1).atTime(6, 0));
//    night.setPrevious(day);
//    day.setNext(night);
    dayScheme.setTimeSlots(List.of(day, night));

    return dayScheme;
  }
}
