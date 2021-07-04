package de.mankianer.todoassistentmono.dev;

import io.dgraph.DgraphAsyncClient;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Operation;
import io.dgraph.DgraphProto.Version;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DevDgraph {

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
    createSchema();
  }

  private void createSchema(){
    String schema = """
        bloblo: string .
        
        type User {
          name: string!
          todo: string
          bloblo: string
        }
        """;
    Operation operation = Operation.newBuilder().setSchema(schema).setRunInBackground(false).build();
    dgraphClient.alter(operation);
  }
}
