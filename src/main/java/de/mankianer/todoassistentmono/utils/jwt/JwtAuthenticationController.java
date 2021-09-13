package de.mankianer.todoassistentmono.utils.jwt;

import de.mankianer.todoassistentmono.utils.jwt.models.JwtRequest;
import de.mankianer.todoassistentmono.utils.jwt.models.JwtResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {

  @Value("${JWT_TOKEN_VALIDITY}")
  public int maxAge;

  private final AuthenticationManager authenticationManager;

  private final JwtTokenUtil jwtTokenUtil;

  private final UserDetailsService userDetailsService;

  public JwtAuthenticationController(
      AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      UserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @RequestMapping(value = "/token/", method = RequestMethod.POST)
  public void login(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

//    response.addCookie(cookie);
    response.setHeader("Set-Cookie",
        JwtTokenUtil.AuthorizationHeaderName + "=Bearer+" + token + "; Max-Age=" + jwtTokenUtil.JWT_TOKEN_VALIDITY + "; Path=/; HttpOnly; SameSite=None; Secure;");
  }

  @RequestMapping(value = "/token/del", method = RequestMethod.DELETE)
  public ResponseEntity<?> logout(HttpServletResponse response) throws Exception {
    response.setHeader("Set-Cookie",
        JwtTokenUtil.AuthorizationHeaderName + "=; Max-Age=" + jwtTokenUtil.JWT_TOKEN_VALIDITY + "; Path=/; HttpOnly; SameSite=None; Secure;");
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/token/valid")
  public boolean isLogin(Authentication authentication) {
    return authentication != null;
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

  private String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return value;
  }
}
