package de.mankianer.todoassistentmono.dev;

import de.mankianer.todoassistentmono.config.models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DevUserDetailsService implements UserDetailsService {

  @Value("${username:user}")
  private String username;
  @Value("${password:password}")
  private String password;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if(username.toLowerCase().equals(this.username)) throw new UsernameNotFoundException("User Not Found");
    return new CustomUserDetails(this.username, password);
  }
}
