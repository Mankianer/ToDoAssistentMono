package de.mankianer.todoassistentmono.utils.jwt;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BrokenCorsHeaderHelper implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    if(req.getMethod().equalsIgnoreCase("OPTIONS")){
      HttpServletResponse res = (HttpServletResponse) response;
      res.addHeader("Access-Control-Allow-Headers", "*");
      res.addHeader("Access-Control-Allow-Headers", "Set-Cookie");
    }
    chain.doFilter(request, response);

  }
}
