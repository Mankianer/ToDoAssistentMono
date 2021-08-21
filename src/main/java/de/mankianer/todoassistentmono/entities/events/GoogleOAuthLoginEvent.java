package de.mankianer.todoassistentmono.entities.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleOAuthLoginEvent {
  private String username, newAccessToken;
}
