package de.mankianer.todoassistentmono.utils.dgraph;

import com.google.gson.Gson;
import de.mankianer.todoassistentmono.config.dgraph.Schema;
import de.mankianer.todoassistentmono.entities.models.DgraphEntity;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Operation;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DgraphService {

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
    createSchema();
  }

  private void createSchema() {
    Operation operation = Operation.newBuilder().setSchema(Schema.PREDICATES)
        .setRunInBackground(false).build();
    dgraphClient.alter(operation);
  }

  public <T extends DgraphEntity> T saveToDGraph(@NonNull T entity) {
    DgraphRepo<T> dgraphRepo = new DgraphRepo<>(getDgraphClient());
    return dgraphRepo.saveToDGraph(entity);
  }

  public <T extends DgraphEntity> T findByUid(@NonNull String uid) {
    DgraphRepo<T> dgraphRepo = new DgraphRepo<>(getDgraphClient());
    return dgraphRepo.findByUid(uid);
  }

  public <T extends DgraphEntity> boolean deleteFromDGraph(@NonNull String uid) {
    DgraphRepo<T> dgraphRepo = new DgraphRepo<>(getDgraphClient());
    return dgraphRepo.deleteFromDGraphByUid(uid);
  }


}
