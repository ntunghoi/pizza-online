package com.ntunghoi.pizza.engine.handlers;

import java.util.List;

import com.ntunghoi.pizza.engine.entities.OrderEntity;
import com.ntunghoi.pizza.engine.exceptions.InvalidRequestException;
import com.ntunghoi.pizza.engine.models.Order;
import com.ntunghoi.pizza.engine.models.OrderRequest;
import com.ntunghoi.pizza.engine.services.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class OrderHandler {
  private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

  @Autowired
  private OrderService orderService;

  public Mono<ServerResponse> getAllOrders(ServerRequest request) {
    List<Order> orders = orderService.getAllOrders();

    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(orders), OrderEntity.class);
  }

  public Mono<ServerResponse> placeOrder(ServerRequest request) {
    Mono<OrderRequest> orderRequestRaw = request.bodyToMono(OrderRequest.class);

    return orderRequestRaw.flatMap(orderRequest -> {
      try {
        Order order = orderService.save(orderRequest);
        logger.info("Saved order with ID: {}", order.getId());
        return ServerResponse.status(HttpStatus.CREATED).body(Mono.just(order), Order.class);
      } catch (InvalidRequestException ire) {
        return ServerResponse.badRequest().build();
      }
    });
  }
}
