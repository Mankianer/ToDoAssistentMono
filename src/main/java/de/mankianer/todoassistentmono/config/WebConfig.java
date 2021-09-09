package de.mankianer.todoassistentmono.config;

import de.mankianer.todoassistentmono.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${cors.allowedOriginPatterns:https://localhost:4200}")
  private String string;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowCredentials(true).allowedOriginPatterns(string)
        .allowedHeaders(JwtTokenUtil.AuthorizationHeaderName, "Set-Cookie", "*").exposedHeaders("*", "Set-Cookie")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
  }
}
