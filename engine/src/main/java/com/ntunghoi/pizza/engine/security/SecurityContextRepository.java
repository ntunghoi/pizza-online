package com.ntunghoi.pizza.engine.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
  private static final Logger logger = LoggerFactory.getLogger(SecurityContextRepository.class);
  private static final String TOKEN_PREFIX = "Bearer ";

  @Autowired
  private AuthenticationManager authenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext context) {
    throw new UnsupportedOperationException("Not support yet");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {
    ServerHttpRequest request = swe.getRequest();
    Optional<String> rawAuthToken = getAuthToken(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    if (rawAuthToken.isPresent()) {
      String[] tokens = rawAuthToken.get().split(":");
      if (tokens.length == 3) {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(tokens[2]));
        Authentication authToken = new UsernamePasswordAuthenticationToken(tokens[0], tokens[1], authorities);

        return authenticationManager.authenticate(authToken)
            .map(authentication -> new SecurityContextImpl(authentication));
      }
    }

    return Mono.empty();
  }

  private Optional<String> getAuthToken(String authHeader) {
    logger.info(String.format("Auth Header: %s", authHeader));
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      return Optional.of(authHeader.replace(TOKEN_PREFIX, ""));
    }

    return Optional.empty();
  }
}
