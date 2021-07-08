package de.mankianer.todoassistentmono.utils;

import de.mankianer.todoassistentmono.config.dgraph.Schema;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Operation;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ToDoAssistentDgraphClientBean {

  @Value("${dgraph.adresse.name:localhost}")
  private String dgraphHost;
  @Value("${dgraph.adresse.port:9080}")
  private int port;
  @Getter
  private DgraphClient dgraphClient;

  @PostConstruct
  public void init(){
    ManagedChannel channel = ManagedChannelBuilder.forAddress(dgraphHost, port).usePlaintext().build();
    DgraphStub stub = DgraphGrpc.newStub(channel);
    dgraphClient = new DgraphClient(stub);
//    log.info("Dgraph Version: " + dgraphClient.checkVersion().getTag());
//    createSchema();
  }

  private void createSchema(){
    Operation operation = Operation.newBuilder().setSchema(Schema.PREDICATES + "\n" + Schema.TYPES).setRunInBackground(false).build();
    dgraphClient.alter(operation);
  }
}
