package de.mankianer.todoassistentmono.google.services;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gson.Gson;
import de.mankianer.todoassistentmono.google.events.GoogleOAuthLoginEvent;
import de.mankianer.todoassistentmono.google.models.ClientCredential;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GoogleService {

  /**
   * Path to Credentials for the GoogleApi from the GoogleProject in credentials.json-file
   */
  @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
  private String clientCredentialsJsonPath;

  @Value("${MainUserLocalID:main}")
  private String mainUserLocalID;

  /**
   * called by redirection of OAuth2 from GoogleApi via User
   */
  @Value("${googleOAuth2RedirectUri}")
  private String redirectUri;

  /**
   * Credentials for the GoogleApi from the GoogleProject
   */
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

  /**
   * try to load the MainUserGoogleApiToken from memory
   * if not will try to Request via Console
   * @return
   */
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

  /**
   * will try to Request GoogleApiToken via Console
   * @throws IOException
   */
  protected void loadMainUser() throws IOException {
    Credential credential = authorizationCodeFlow.loadCredential(mainUserLocalID);
    if (credential == null) {
      System.out.println("GoogleUri: " + authorizationCodeFlow.newAuthorizationUrl()
          .setRedirectUri(redirectUri));
    }
    System.out.println("cred: " + credential);
  }

  /**
   * try to load Credentials for the GoogleApi from the GoogleProject via credentials.json-file
   * @throws IOException
   */
  protected void loadClientCredentials() throws IOException {
    Path path = Paths.get(clientCredentialsJsonPath);
    String json = Files.readString(path);
    clientCredential = new Gson().fromJson(json, ClientCredential.Web.class).getWeb();
  }

  /**
   * prepare to register User GoogleApiTokens
   */
  protected void loadAuthorizationCodeFlow() {
    authorizationCodeFlow = new GoogleAuthorizationCodeFlow(
        new NetHttpTransport(),
        new GsonFactory(),
        clientCredential.getClient_id(),
        clientCredential.getClient_secret(),
        Collections.singleton(CalendarScopes.CALENDAR));
  }

  /**
   * call by redirection of OAuth2 from GoogleApi via User
   * @param code
   */
  public void onCallbackMainUser(@NonNull String code) {
    onCallbackUser(code, mainUserLocalID);
  }

  /**
   * call by redirection of OAuth2 from GoogleApi via User
   * @param code
   */
  public void onCallbackUser(@NonNull String code, String userID) {
    try {
      AuthorizationCodeTokenRequest authorizationCodeTokenRequest = authorizationCodeFlow
          .newTokenRequest(code);

      authorizationCodeTokenRequest.setRedirectUri(redirectUri);

      Credential credential = authorizationCodeFlow
          .createAndStoreCredential(authorizationCodeTokenRequest.execute(), userID);
      userTokenMap.put(userID, credential.getAccessToken());
      applicationEventPublisher.publishEvent(new GoogleOAuthLoginEvent(userID,credential.getAccessToken(),credential));

    } catch (IOException e) {
      log.warn(e);
    }
  }
}
