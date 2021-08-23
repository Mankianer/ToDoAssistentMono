package de.mankianer.todoassistentmono.utils;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.config.dgraph.Schema;
import de.mankianer.todoassistentmono.entities.models.DGraphEntity;
import de.mankianer.todoassistentmono.entities.models.TimeSlot;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Operation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ToDoAssistentDgraphClientBean {

  @Value("${dgraph.adresse.name:localhost}")
  private String dgraphHost;
  @Value("${dgraph.adresse.port:9080}")
  private int port;
  @Getter
  private DgraphClient dgraphClient;
  private Gson gson;

  @PostConstruct
  public void init() {
    gson = new Gson();
    ManagedChannel channel = ManagedChannelBuilder.forAddress(dgraphHost, port).usePlaintext()
        .build();
    DgraphStub stub = DgraphGrpc.newStub(channel);
    dgraphClient = new DgraphClient(stub);

//    log.info("Dgraph Version: " + dgraphClient.checkVersion().getTag());
//    createSchema();
  }

  private void createSchema() {
    Operation operation = Operation.newBuilder().setSchema(Schema.PREDICATES + "\n" + Schema.TYPES)
        .setRunInBackground(false).build();
    dgraphClient.alter(operation);
  }

  public <T extends DGraphEntity> T saveToDGraph(T entity) {
    Gson gson = new Gson();
    Transaction txn = dgraphClient.newTransaction();
    try {
      String json = gson.toJson(entity);
      Mutation mutation = Mutation.newBuilder().setSetJson(ByteString.copyFromUtf8(json.toString()))
          .setCommitNow(true)
          .build();
      Response response = txn.mutate(mutation);
      Collection<String> uids = response.getUidsMap().values();
      if (uids.size() > 1) {
        log.warn("more than 1 uid was returned!");
        response.getUidsMap().forEach((k, v) -> log.warn("{}:{}", k, v));
      }
      entity.setUid(uids.stream().findFirst().orElse(null));
      return entity;
    } finally {
      txn.discard();
    }

  }

  public <T extends DGraphEntity> T[] findByUid(String uid, Class<T> clazz) {
    String fields = "";
    try {
      fields = clazz.getDeclaredConstructor().newInstance().getAllFields().stream()
          .map(field -> {
            return field.getName() + "\n";
          }).collect(Collectors.joining());
    } catch (Exception e) {
      log.warn(e);
      fields = "uid";
    }
    String query = """
        query findByUid($uid: string) {
           findByUid(func: uid($uid)) {
           """
        + fields +
        """
           }
         }""";
    Map<String, String> vars = Collections.singletonMap("$uid", uid);
    Response response = getDgraphClient().newReadOnlyTransaction()
        .queryWithVars(query, vars);

    String json = response.getJson().toStringUtf8();
    json = json.substring("{\"findByUid\":".length(), json.length() - 1);
    log.info("response Json:" + json);
    T[] byUid = gson.fromJson(json, (Type) clazz.arrayType());
    return byUid;
  }

}
