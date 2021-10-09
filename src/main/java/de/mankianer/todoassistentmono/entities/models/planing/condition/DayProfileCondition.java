package de.mankianer.todoassistentmono.entities.models.planing.condition;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import de.mankianer.todoassistentmono.entities.models.DgraphMultiClassEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Triplet;

@Log4j2
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DayProfileCondition extends DgraphMultiClassEntity implements
    DayProfileConditionInterface {

  private String name;


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

  private void validateValues(Map<String, ?> values) throws ValueException {
    List<Triplet<String, ParameterType, Object>> missingValues = getTripletStream(values).filter(t -> t.getValue(2) == null)
        .collect(Collectors.toList());
    List<Triplet<String, ParameterType, Object>> tooMuchValues = getTripletStream(values).filter(t -> t.getValue(1) == null)
        .collect(Collectors.toList());
    List<Triplet<String, ParameterType, Object>> typeMismatch = getTripletStream(values).filter(
        t -> !validateType(t.getValue1(), t.getValue(2))).collect(Collectors.toList());

    if(!missingValues.isEmpty() || !tooMuchValues.isEmpty() || !typeMismatch.isEmpty()) {
      throw new ValueException(missingValues, tooMuchValues, typeMismatch);
    }
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

  public enum ParameterType {
    NUMBER, STRING, LIST
  }

  public class ValueException extends Exception {

    private List<Triplet<String, ParameterType, Object>> missingValues, tooMuchValues, typeMismatch;

    public ValueException(
        List<Triplet<String, ParameterType, Object>> missingValues,
        List<Triplet<String, ParameterType, Object>> tooMuchValues,
        List<Triplet<String, ParameterType, Object>> typeMismatch) {
      super(CreateValueExceptionMessage(missingValues, tooMuchValues, typeMismatch));
      this.missingValues = missingValues;
      this.tooMuchValues = tooMuchValues;
      this.typeMismatch = typeMismatch;
    }
  }

  public static String CreateValueExceptionMessage(
      List<Triplet<String, ParameterType, Object>> missingValues,
      List<Triplet<String, ParameterType, Object>> tooMuchValues,
      List<Triplet<String, ParameterType, Object>> typeMismatchValues) {
    String ret = "";
    if(!missingValues.isEmpty()){
      ret += "Missing Values:\n";
      for (Triplet<String, ParameterType, Object> missingValue : missingValues) {
        ret += "\tname:" + missingValue.getValue0() + "\n";
      }
    }
    if(!tooMuchValues.isEmpty()){
      ret += "Too much Values:\n";
      for (Triplet<String, ParameterType, Object> tooMuchValue : tooMuchValues) {
        ret += "\tname:" + tooMuchValue.getValue0() + "\n";
      }
    }
    if(!typeMismatchValues.isEmpty()){
      ret += "Type mismatching Values:\n";
      for (Triplet<String, ParameterType, Object> typeMismatchValue : typeMismatchValues) {
        ret += "\tname:" + typeMismatchValue.getValue0() + " expected: "
            + typeMismatchValue.getValue1().name() + " value: " + typeMismatchValue.getValue2()
            .toString() + "\n";
      }
    }
    return ret;
  }
}
