package com.ntunghoi.pizza.engine.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ntunghoi.pizza.engine.models.OrderItemRequest;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(generator = "UUID")
  @ColumnDefault("random_uuid()")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  @JsonIgnoreProperties("items")
  private OrderEntity order;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "price")
  private double price;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  public static OrderItemEntity from(OrderEntity order, OrderItemRequest orderLineRequest) {
    return new OrderItemEntity(order, orderLineRequest.getName(), orderLineRequest.getQuantity(),
        orderLineRequest.getPrice());
  }

  public OrderItemEntity() {
    // default constructor
  }

  private OrderItemEntity(OrderEntity order, String name, int quantity, double price) {
    this.order = order;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public UUID getId() {
    return id;
  }

  public OrderEntity getOrder() {
    return order;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }
}
