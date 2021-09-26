package de.mankianer.todoassistentmono.utils.google.events;

import com.google.api.client.auth.oauth2.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class GoogleOAuthLoginEvent {
  private String username, newAccessToken;
  private Credential credential;
}
