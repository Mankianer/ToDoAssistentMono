package de.mankianer.todoassistentmono.google;

import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import de.mankianer.todoassistentmono.google.models.ClientCredential;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {

  @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
  private String clientCredentialsJsonPath;

  @Getter
  private ClientCredential clientCredential;

  @PostConstruct
  public void init() throws IOException {
    loadClientCredentials();
  }

  protected void loadClientCredentials() throws IOException {
    Path path = Paths.get(clientCredentialsJsonPath);
    String json = Files.readString(path);
    clientCredential = new Gson().fromJson(json, ClientCredential.Web.class).getWeb();
  }
}
