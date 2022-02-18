package com.ntunghoi.pizza.engine.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ntunghoi.pizza.engine.entities.OrderEntity;
import com.ntunghoi.pizza.engine.entities.OrderItemEntity;
import com.ntunghoi.pizza.engine.exceptions.InvalidRequestException;
import com.ntunghoi.pizza.engine.models.Order;
import com.ntunghoi.pizza.engine.models.OrderItem;
import com.ntunghoi.pizza.engine.models.OrderItemRequest;
import com.ntunghoi.pizza.engine.models.OrderRequest;
import com.ntunghoi.pizza.engine.repositories.OrderRepository;
import com.ntunghoi.pizza.engine.services.OrderService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
  private OrderRepository orderRepository;
  private ModelMapper modelMapper;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
    this.orderRepository = orderRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<Order> getAllOrders() {
    return orderRepository.findAll()
        .stream()
        .map(this::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public Order save(OrderRequest orderRequest) throws InvalidRequestException {
    List<OrderItemRequest> orderItems = orderRequest.getItems();
    if (orderItems == null || orderItems.size() == 0) {
      throw new InvalidRequestException("Missing order items");
    }
    if (orderItems.stream().anyMatch(orderItem -> !orderItem.isValid())) {
      throw new InvalidRequestException("Invalid order item request");
    }

    orderRepository.save(OrderEntity.from(orderRequest));
    return fromEntity(orderRepository.save(OrderEntity.from(orderRequest)));
  }

  private Order fromEntity(OrderEntity entity) {
    Set<OrderItem> items = entity.getItems().stream().map(this::fromEntity).collect(Collectors.toSet());
    Order order = modelMapper.map(entity, Order.class);
    order.setItems(items);

    return order;
  }

  private OrderItem fromEntity(OrderItemEntity entity) {
    return modelMapper.map(entity, OrderItem.class);
  }
}
