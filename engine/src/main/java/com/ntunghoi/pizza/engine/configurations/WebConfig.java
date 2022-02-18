package com.ntunghoi.pizza.engine.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.ntunghoi.pizza.engine.handlers.OrderHandler;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
  private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

  @Bean
  public RouterFunction<ServerResponse> orderRoutes(OrderHandler orderHandler) {
    return route()
        .before(request -> {
          logger.info(String.format("Request Path: %s", request.path()));
          return request;
        })
        .GET("/orders", accept(MediaType.APPLICATION_JSON), orderHandler::getAllOrders)
        .POST("/orders", accept(MediaType.APPLICATION_JSON), orderHandler::placeOrder)
        .build();
  }
}
