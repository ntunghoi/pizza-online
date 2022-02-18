package com.ntunghoi.pizza.engine.services;

import java.util.List;

import com.ntunghoi.pizza.engine.exceptions.InvalidRequestException;
import com.ntunghoi.pizza.engine.models.Order;
import com.ntunghoi.pizza.engine.models.OrderRequest;

public interface OrderService {
  List<Order> getAllOrders();

  Order save(OrderRequest orderRequest) throws InvalidRequestException;
}
