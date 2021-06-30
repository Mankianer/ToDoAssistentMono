package de.mankianer.todoassistentmono.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
  private String jwttoken;
}
