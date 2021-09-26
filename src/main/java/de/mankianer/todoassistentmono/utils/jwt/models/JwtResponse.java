package de.mankianer.todoassistentmono.utils.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class JwtResponse {
  private String jwttoken;
}
