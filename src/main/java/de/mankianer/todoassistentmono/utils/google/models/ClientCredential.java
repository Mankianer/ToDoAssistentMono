package de.mankianer.todoassistentmono.utils.google.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClientCredential {
  private String client_id, project_id, auth_uri, token_uri, auth_provider_x509_cert_url, client_secret;

  @Data
  @EqualsAndHashCode(callSuper=false)
  public class Web{
    private ClientCredential web;
  }
}

