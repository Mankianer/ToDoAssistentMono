package de.mankianer.todoassistentmono.utils;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import de.mankianer.todoassistentmono.config.dgraph.Schema;
import de.mankianer.todoassistentmono.entities.models.DGraphEntity;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Mutation;
import io.dgraph.DgraphProto.Operation;
import io.dgraph.DgraphProto.Response;
import io.dgraph.Transaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
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

  @PostConstruct
  public void init() {
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
      }
      entity.setUid(uids.stream().findFirst().orElse(null));
      return entity;
    } finally {
      txn.discard();
    }

  }
}
