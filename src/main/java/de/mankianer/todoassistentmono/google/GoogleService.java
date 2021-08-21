package de.mankianer.todoassistentmono.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gson.Gson;
import de.mankianer.todoassistentmono.google.models.ClientCredential;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {

  @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
  private String clientCredentialsJsonPath;

  @Value("${MainUserLocalID:main}")
  private String mainUserLocalID;

  @Getter
  private ClientCredential clientCredential;
  private AuthorizationCodeFlow authorizationCodeFlow;

  @PostConstruct
  public void init() throws IOException {
    loadClientCredentials();
    loadAuthorizationCodeFlow();
    loadMainUser();
  }

  protected void loadMainUser() throws IOException {
    Credential credential = authorizationCodeFlow.loadCredential(mainUserLocalID);
    if (credential == null) {

      System.out.println("GoogleUri: " + authorizationCodeFlow.newAuthorizationUrl().setRedirectUri("https://localhost:8080/google/oauth2/"));
    }
    System.out.println("cred: " + credential);
  }

  protected void loadClientCredentials() throws IOException {
    Path path = Paths.get(clientCredentialsJsonPath);
    String json = Files.readString(path);
    clientCredential = new Gson().fromJson(json, ClientCredential.Web.class).getWeb();
  }

  protected void loadAuthorizationCodeFlow() {
    authorizationCodeFlow = new AuthorizationCodeFlow.Builder(
        BearerToken.authorizationHeaderAccessMethod(),
        new NetHttpTransport(),
        new GsonFactory(),
        new GenericUrl(clientCredential.getToken_uri()),
        new BasicAuthentication(clientCredential.getClient_id(),
            clientCredential.getClient_secret()),
        clientCredential.getClient_id(),
        clientCredential.getAuth_uri()
    ).setScopes(Collections.singleton(CalendarScopes.CALENDAR)).build();
  }
}
