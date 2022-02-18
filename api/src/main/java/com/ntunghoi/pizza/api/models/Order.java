package com.ntunghoi.pizza.api.models;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Order {
  private UUID id;
  private Set<OrderItem> items;
  private Date createdAt;
  private Date updatedAt;

  public Order() {
    // default constructor
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Set<OrderItem> getItems() {
    return items;
  }

  public void setItems(Set<OrderItem> items) {
    this.items = items;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
