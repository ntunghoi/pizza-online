package com.ntunghoi.pizza.engine.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ntunghoi.pizza.engine.models.OrderItemRequest;
import com.ntunghoi.pizza.engine.models.OrderRequest;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
public class OrderEntity {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(generator = "UUID")
  @ColumnDefault("random_uuid()")
  private UUID id;

  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonIgnoreProperties("order")
  private Set<OrderItemEntity> items = new HashSet<OrderItemEntity>();

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  public static OrderEntity from(OrderRequest orderRequest) {
    return new OrderEntity(orderRequest.getItems());
  }

  public OrderEntity() {
    // default constructor
  }

  private OrderEntity(List<OrderItemRequest> orderItems) {
    orderItems.stream().forEach(item -> items.add(OrderItemEntity.from(this, item)));
  }

  public UUID getId() {
    return id;
  }

  public Set<OrderItemEntity> getItems() {
    return items;
  }

  public void setItems(Set<OrderItemEntity> items) {
    this.items = items;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }
}
