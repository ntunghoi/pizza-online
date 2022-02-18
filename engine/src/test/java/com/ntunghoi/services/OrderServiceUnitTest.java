package com.ntunghoi.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Set;

import com.ntunghoi.pizza.engine.entities.OrderEntity;
import com.ntunghoi.pizza.engine.entities.OrderItemEntity;
import com.ntunghoi.pizza.engine.exceptions.InvalidRequestException;
import com.ntunghoi.pizza.engine.models.OrderItemRequest;
import com.ntunghoi.pizza.engine.models.OrderRequest;
import com.ntunghoi.pizza.engine.repositories.OrderRepository;
import com.ntunghoi.pizza.engine.services.OrderService;
import com.ntunghoi.pizza.engine.services.impl.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {
  @Mock
  OrderRepository orderRepository;

  OrderService orderService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    orderService = new OrderServiceImpl(orderRepository, new ModelMapper());
  }

  @Test
  void whenMissingOrderItemRequest_thenInvalidRequestException() {
    InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
        () -> orderService.save(new OrderRequest()));
    assertEquals(thrown.getMessage(), "Missing order items");
  }

  @Test
  void whenNoOrderItemRequest_thenInvalidRequestException() {
    InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
        () -> orderService.save(new OrderRequest(List.of())));
    assertEquals(thrown.getMessage(), "Missing order items");
  }

  @Test
  void whenValidOrderRequest_thenCorrect() {
    String name = "name";
    int quantity = 3;
    double price = 3.3;

    OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(name, quantity, price)));

    Set<OrderItemEntity> ItemEntities = Set.of(new OrderItemEntity());
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setItems(ItemEntities);
    Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

    assertDoesNotThrow(() -> orderService.save(orderRequest));
  }

  @Test
  void whenInvalidOrderItemName_thenInvalidRequestException() {
    int quantity = 3;
    double price = 3.3;

    OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(null, quantity, price)));
    InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
        () -> orderService.save(orderRequest));
    assertEquals(thrown.getMessage(), "Invalid order item request");
  }

  @Test
  void whenInvalidOrderItemQuantity_thenInvalidRequestException() {
    String name = "name";
    int quantity = 0;
    double price = 3.3;

    OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(name, quantity, price)));
    InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
        () -> orderService.save(orderRequest));
    assertEquals(thrown.getMessage(), "Invalid order item request");
  }

  @Test
  void whenInvalidOrderItemPrice_thenInvalidRequestException() {
    String name = "name";
    int quantity = 1;
    double price = 0;

    OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(name, quantity, price)));
    InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
        () -> orderService.save(orderRequest));
    assertEquals(thrown.getMessage(), "Invalid order item request");
  }
}
