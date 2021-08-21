package de.mankianer.todoassistentmono.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gson.Gson;
import de.mankianer.todoassistentmono.entities.events.GoogleOAuthLoginEvent;
import de.mankianer.todoassistentmono.google.models.ClientCredential;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GoogleService {

  @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
  private String clientCredentialsJsonPath;

  @Value("${MainUserLocalID:main}")
  private String mainUserLocalID;

  @Value("${googleOAuth2RedirectUri}")
  private String redirectUri;

  @Getter
  private ClientCredential clientCredential;

  @Getter
  private GoogleAuthorizationCodeFlow authorizationCodeFlow;

  private Map<String, String> userTokenMap;

  private final ApplicationEventPublisher applicationEventPublisher;

  public GoogleService(
      ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @PostConstruct
  public void init() throws IOException {
    userTokenMap = new HashMap<>();
    loadClientCredentials();
    loadAuthorizationCodeFlow();
    loadMainUser();
  }

  public String getMainUserToke(){
    if(userTokenMap.containsKey(mainUserLocalID)){
      return userTokenMap.get(mainUserLocalID);
    }
    log.warn("Main User have to logged in with Google!");
    try {
      loadMainUser();
    } catch (IOException e) {
      log.error(e);
    }
    return null;
  }

  protected void loadMainUser() throws IOException {
    Credential credential = authorizationCodeFlow.loadCredential(mainUserLocalID);
    if (credential == null) {
      System.out.println("GoogleUri: " + authorizationCodeFlow.newAuthorizationUrl()
          .setRedirectUri(redirectUri));
    }
    System.out.println("cred: " + credential);
  }

  protected void loadClientCredentials() throws IOException {
    Path path = Paths.get(clientCredentialsJsonPath);
    String json = Files.readString(path);
    clientCredential = new Gson().fromJson(json, ClientCredential.Web.class).getWeb();
  }

  protected void loadAuthorizationCodeFlow() {
    authorizationCodeFlow = new GoogleAuthorizationCodeFlow(
        new NetHttpTransport(),
        new GsonFactory(),
        clientCredential.getClient_id(),
        clientCredential.getClient_secret(),
        Collections.singleton(CalendarScopes.CALENDAR));
  }

  public void onCallbackMainUser(@NonNull String code) {
    onCallbackUser(code, mainUserLocalID);
  }

  public void onCallbackUser(@NonNull String code, String userID) {
    try {
      AuthorizationCodeTokenRequest authorizationCodeTokenRequest = authorizationCodeFlow
          .newTokenRequest(code);

      authorizationCodeTokenRequest.setRedirectUri(redirectUri);

      Credential credential = authorizationCodeFlow
          .createAndStoreCredential(authorizationCodeTokenRequest.execute(), userID);
      userTokenMap.put(userID, credential.getAccessToken());
      applicationEventPublisher.publishEvent(new GoogleOAuthLoginEvent(userID,credential.getAccessToken()));

    } catch (IOException e) {
      log.warn(e);
    }
  }
}
