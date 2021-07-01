package de.mankianer.todoassistentmono.dev;

import de.mankianer.todoassistentmono.config.models.CustomUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DevUserDetailsService implements UserDetailsService {

  @Value("${dev.username:user}")
  private String username;
  @Value("${dev.password:password}")
  private String password;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug(username + " " + password);
    if(!username.toLowerCase().equals(this.username.toLowerCase())) throw new UsernameNotFoundException("User Not Found");
    return new CustomUserDetails(password, username);
  }
}
