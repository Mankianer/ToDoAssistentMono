package de.mankianer.todoassistentmono.utils.dgraph.query;

import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Builder
public class DQuery {

  @NonNull
  private String queryname;
  @NonNull
  private String functionName;
  @NonNull
  private Class<? extends DgraphEntity> actualTypeArgument;
  @NonNull
  private String fieldName, paramName;
  @NonNull
  private DGraphType paramType;
  @NonNull
  private DQueryRootFilter.RootTypes rootFilter;

  public String buildQueryString() {
    DQueryFunction function = DQueryFunction.builder().functionName(functionName).queryRootFilter(
        DQueryRootFilter.builder().fieldName(fieldName).paramName(paramName).rootTypes(rootFilter)
            .build()).build();
    return "query " + queryname + "($" + paramName + ": " + paramType.name() + " ) {\n" +
        function.buildFunctionString() + " {\n" +
        DGraphQueryUtiles.convertQueryMapToField(getQueryMap()) +
        "}\n}";
  }

  private Map getQueryMap() {
    return DGraphQueryUtiles.getFieldMap(actualTypeArgument);
  }

}
