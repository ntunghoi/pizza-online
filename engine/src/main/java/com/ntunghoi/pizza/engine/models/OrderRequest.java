package com.ntunghoi.pizza.engine.models;

import java.util.List;

public class OrderRequest {
  private List<OrderItemRequest> items;

  public OrderRequest() {
    // default constructor
  }

  public OrderRequest(List<OrderItemRequest> items) {
    this.items = items;
  }

  public List<OrderItemRequest> getItems() {
    return items;
  }
}
