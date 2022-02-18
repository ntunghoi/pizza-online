package com.ntunghoi.pizza.engine.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String username = authentication.getPrincipal().toString();
    String password = authentication.getCredentials().toString();
    logger.info(String.format("Authentication: %s", username));
    if (username.equals(password)) {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
          password, authentication.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      return Mono.just(authenticationToken);
    }

    return Mono.empty();
  }
}
