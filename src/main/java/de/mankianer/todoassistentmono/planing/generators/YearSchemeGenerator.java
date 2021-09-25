package de.mankianer.todoassistentmono.planing.generators;

import de.mankianer.todoassistentmono.entities.models.YearScheme;

public interface YearSchemeGenerator {
  public YearScheme createYearScheme(int year);
  public String getName();
}
