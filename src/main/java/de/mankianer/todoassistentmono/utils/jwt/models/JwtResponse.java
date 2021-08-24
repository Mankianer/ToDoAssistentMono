package de.mankianer.todoassistentmono.utils.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
  private String jwttoken;
}
