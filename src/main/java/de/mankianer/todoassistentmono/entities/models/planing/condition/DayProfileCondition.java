package de.mankianer.todoassistentmono.entities.models.planing.condition;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.javatuples.Triplet;


@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DayProfileCondition extends DgraphMultiClassEntity implements
    DayProfileConditionInterface {

  private String name = "";


  public static Integer getIntegerFromNumber(String name, Map<String, ?> values) {
    Object ret = values.get(name);
    return ret instanceof Integer ? (Integer) ret : ((Double) ret).intValue();
  }

  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public abstract Map<String, ParameterType> getParameterTypeMap();

  public void applyValues(Map<String, ?> values) throws ValueException {
    validateValues(values);
    passValues(values);
  }

  public static String CreateValueExceptionMessage(
      List<Triplet<String, ParameterType, Object>> missingValues,
      List<Triplet<String, ParameterType, Object>> tooMuchValues,
      List<Triplet<String, ParameterType, Object>> typeMismatchValues,
      List<Triplet<String, ParameterType, Object>> unallowedValues) {
    String ret = "";
    if (!missingValues.isEmpty()) {
      ret += "Missing Values:\n";
      for (Triplet<String, ParameterType, Object> missingValue : missingValues) {
        ret += "\tname:" + missingValue.getValue0() + "\n";
      }
    }
    if (!tooMuchValues.isEmpty()) {
      ret += "Too much Values:\n";
      for (Triplet<String, ParameterType, Object> tooMuchValue : tooMuchValues) {
        ret += "\tname:" + tooMuchValue.getValue0() + "\n";
      }
    }
    if (!typeMismatchValues.isEmpty()) {
      ret += "Type mismatching Values:\n";
      for (Triplet<String, ParameterType, Object> typeMismatchValue : typeMismatchValues) {
        ret += "\tname:" + typeMismatchValue.getValue0() + " expected: "
            + typeMismatchValue.getValue1().name() + " value: " + typeMismatchValue.getValue2()
            .toString() + "\n";
      }
    }
    if (!unallowedValues.isEmpty()) {
      ret += "Unallowed Values:\n";
      for (Triplet<String, ParameterType, Object> unallowedValue : unallowedValues) {
        ret += "\tname:" + unallowedValue.getValue0() + " expected: "
            + unallowedValue.getValue1().name() + " value: " + unallowedValue.getValue2()
            .toString() + "\n";
      }
    }
    return ret;
  }

  private Stream<Triplet<String, ParameterType, Object>> getTripletStream(Map<String, ?> values) {
    return getParameterTypeMap().keySet()
        .stream().map(name -> {
          return new Triplet(name, getParameterTypeMap().get(name), values.get(name));
        });
  }

  protected abstract void passValues(Map<String, ?> values);

  private boolean validateType(ParameterType parameterType, Object value) {
    switch (parameterType) {
      case NUMBER:
        return value instanceof Integer | value instanceof Double;
      case STRING:
        return value instanceof String;
      case LIST:
        return value instanceof List;
    }
    return false;
  }

  public void validateValues(Map<String, ?> values) throws ValueException {
    List<Triplet<String, ParameterType, Object>> missingValues = getTripletStream(values).filter(
            t -> t.getValue(2) == null)
        .collect(Collectors.toList());
    List<Triplet<String, ParameterType, Object>> tooMuchValues = getTripletStream(values).filter(
            t -> t.getValue(1) == null)
        .collect(Collectors.toList());
    List<Triplet<String, ParameterType, Object>> typeMismatch = getTripletStream(values).filter(
        t -> !validateType(t.getValue1(), t.getValue(2))).collect(Collectors.toList());
    List<Triplet<String, ParameterType, Object>> unallowedValues = getTripletStream(values).filter(
        t -> !validateValue(t.getValue1(), t.getValue(2))).collect(Collectors.toList());

    if (!missingValues.isEmpty() || !tooMuchValues.isEmpty() || !typeMismatch.isEmpty()) {
      throw new ValueException(missingValues, tooMuchValues, typeMismatch, unallowedValues);
    }
  }

  private boolean validateValue(ParameterType parameterType, Object value) {
    if (validateType(parameterType, value)) {
      if (parameterType.allowedValues != null) {
        switch (parameterType) {
          case NUMBER:
            return validateNumber(parameterType, value);
          case STRING:
            return parameterType.allowedValues.contains(value.toString());
          case LIST:
            return validateList(parameterType, value);
        }
      }
    }
    return true;
  }

  private boolean validateList(ParameterType parameterType, Object value) {
    List valList = (List) value;
    for (Object o : valList) {
      if (!parameterType.allowedValues.contains(o.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Stupid validation: Looks parameterType.allowValues up for containing value
   */
  private boolean validateNumber(ParameterType parameterType, Object value) {
    if (value instanceof Integer) {
      int val = (Integer) value;
      return parameterType.allowedValues.contains("" + val);
    } else if (value instanceof Double) {
      double val = (Double) value;
      return parameterType.allowedValues.contains("" + val);
    }
    return false;
  }

  public enum ParameterType {
    NUMBER, STRING, LIST;
    @Getter
    @Setter
    List<String> allowedValues = null;

    ParameterType() {
    }

    ParameterType(List<String> allowedValues) {
      this.allowedValues = allowedValues;
    }
  }

  public class ValueException extends Exception {

    private List<Triplet<String, ParameterType, Object>> missingValues, tooMuchValues, typeMismatch, unallowedValues;

    public ValueException(
        List<Triplet<String, ParameterType, Object>> missingValues,
        List<Triplet<String, ParameterType, Object>> tooMuchValues,
        List<Triplet<String, ParameterType, Object>> typeMismatch,
        List<Triplet<String, ParameterType, Object>> unallowedValues) {
      super(
          CreateValueExceptionMessage(missingValues, tooMuchValues, typeMismatch, unallowedValues));
      this.missingValues = missingValues;
      this.tooMuchValues = tooMuchValues;
      this.typeMismatch = typeMismatch;
      this.unallowedValues = unallowedValues;
    }
  }
}
