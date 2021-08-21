package de.mankianer.todoassistentmono.google;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google/oauth2/")
public class GoogleOAuth2CallbackRestController {

  @GetMapping
  public void callBackOauth2(@RequestParam(required = false) String code, @RequestParam(required = false) String error){
    System.out.println("Oauth Callback: " + code + " error: " + error);
  }
}
