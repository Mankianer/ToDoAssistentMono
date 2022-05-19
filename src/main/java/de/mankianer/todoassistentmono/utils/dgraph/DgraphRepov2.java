package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphEntity;
import de.mankianer.todoassistentmono.utils.dgraph.entities.DgraphMultiClassEntity;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTimeTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.gsonadapters.LocalDateTypeAdapter;
import de.mankianer.todoassistentmono.utils.dgraph.query.DGraphQueryUtils;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQuery;
import de.mankianer.todoassistentmono.utils.dgraph.query.DQueryFilterFunction;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DgraphRepov2<T extends DgraphEntity> {

  private final DgraphClient dgraphClient;
  private final Class<T> actualTypeArgument;
  private final Class<? extends DgraphMultiClassEntity> multiClassParent;

  public DgraphRepov2(DgraphClient dgraphClient) {
    this.dgraphClient = dgraphClient;
    actualTypeArgument = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    multiClassParent = DGraphMultiClassEntityUtils.findMultiClassParent(actualTypeArgument);
  }

  public List<T> findByFillters(Map<String, String> parameter,
      @NonNull DQueryFilterFunction rootFilter, DQueryFilterFunction... filters) {
    Map<String, Map> queryMap = DGraphQueryUtils.getQueryMapFromClass(actualTypeArgument, );
    DGraphQueryUtils.createFindByAndFilterFunctionsQuery(rootFilter, queryMap, filters);
  }

  /**
   * @return Map(Uid -> MultiClassIdentifier)
   */
  private Map<String, String> findMultiClassIdentifier(String path, Map<String, String> parameter, @NonNull DQueryFilterFunction rootFilter, DQueryFilterFunction... filters) {
    Map<String, Map> queryMap = DGraphQueryUtils.createQueryMapFromPathForMultiClassIdentifier(
        path);
    String json = findJsonByQuery(
        DGraphQueryUtils.createFindByAndFilterFunctionsQuery(rootFilter, queryMap, filters),
        parameter);
    var identifierMap = DGraphMultiClassEntityUtils.parseMultiClassIdentifierFromJson(json);
    return identifierMap;
  }

  public List<T> findByQuery(Map<String, String> parameter, DQuery query) {
    String json = findJsonByQuery(query, parameter);
    return DGraphEntityParsUtils.parsJson(json, actualTypeArgument);
  }

  public String findJsonByQuery(DQuery query, Map<String, String> parameter) {
    parameter = parameter.entrySet().stream()
        .collect(Collectors.toMap(e -> "$" + e.getKey(), e -> e.getValue()));
    Response response = dgraphClient.newReadOnlyTransaction()
        .queryWithVars(query.buildQueryString(), parameter);
    return DGraphUtils.removeFunctionNameFromJson(response.getJson().toStringUtf8(), query);
  }
}
