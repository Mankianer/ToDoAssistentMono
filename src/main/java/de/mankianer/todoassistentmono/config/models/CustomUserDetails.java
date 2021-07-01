package de.mankianer.todoassistentmono.config.models;

import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  @NonNull
  private String password, username;
  private boolean enabled = true;
  private boolean credentialsNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean accountNonExpired = true;
  private String encodedPassword;

  public String getPassword(){
    return encodedPassword = (encodedPassword != null) ? encodedPassword : new BCryptPasswordEncoder().encode(password);
  }

  private Collection<? extends GrantedAuthority> authorities = List.of(new GrantedAuthority() {
    @Override
    public String getAuthority() {
      return "USERX";
    }
  });
}
