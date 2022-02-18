package com.ntunghoi.pizza.engine.configurations;

import com.ntunghoi.pizza.engine.security.AuthenticationManager;
import com.ntunghoi.pizza.engine.security.SecurityContextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SecurityContextRepository securityContextRepository;

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http.cors().disable()
        .exceptionHandling()
        .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
          swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
          swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        }))
        .and()
        .csrf().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers("/h2-console").permitAll()
        .anyExchange().authenticated()
        .and()
        .build();
  }
}
