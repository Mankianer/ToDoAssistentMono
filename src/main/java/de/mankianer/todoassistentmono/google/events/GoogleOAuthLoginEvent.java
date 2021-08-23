package de.mankianer.todoassistentmono.google.events;

import com.google.api.client.auth.oauth2.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleOAuthLoginEvent {
  private String username, newAccessToken;
  private Credential credential;
}
