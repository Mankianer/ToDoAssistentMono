package de.mankianer.todoassistentmono.config.models;

import java.util.Collection;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  @NonNull
  private String password, username;
  private boolean enabled = true;
  private boolean credentialsNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean accountNonExpired = true;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
}
