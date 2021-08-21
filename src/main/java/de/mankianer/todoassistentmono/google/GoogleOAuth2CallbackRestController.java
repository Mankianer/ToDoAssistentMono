package de.mankianer.todoassistentmono.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/google/oauth2/")
public class GoogleOAuth2CallbackRestController {

  private final GoogleService googleService;

  public GoogleOAuth2CallbackRestController(
      GoogleService googleService) {
    this.googleService = googleService;
  }

  @GetMapping
  public void callBackOauth2(@RequestParam(required = false) String code,
      @RequestParam(required = false) String error) {
    log.info("OAuth input code: {}; error: {}", code, error);
    if (code != null) {
      googleService.onCallbackMainUser(code);
    }
    if (error != null) {
      log.warn("Error on Google OAuth2 Callback: {}", error);
    }
  }
}
