package de.mankianer.todoassistentmono.planing.generators;

import de.mankianer.todoassistentmono.entities.models.YearScheme;
import de.mankianer.todoassistentmono.entities.models.planing.YearSchemeGeneratorConfig;

public class CustomYearSchemeGenerator implements YearSchemeGenerator{

  public YearSchemeGeneratorConfig config;

  @Override
  public YearScheme createYearScheme(int year) {
    return null;
  }

  @Override
  public String getName() {
    return "CustomYearSchemeGenerator";
  }
}
