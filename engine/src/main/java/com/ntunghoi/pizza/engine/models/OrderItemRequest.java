package com.ntunghoi.pizza.engine.models;

public class OrderItemRequest {
  private String name;
  private int quantity;
  private double price;

  public OrderItemRequest() {
    // default constructor
  }

  public OrderItemRequest(String name, int quantity, double price) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  public boolean isValid() {
    return (name != null && name.length() > 0) && quantity > 0 && price > 0;
  }
}
