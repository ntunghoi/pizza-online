package com.ntunghoi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.ntunghoi.pizza.engine.entities.OrderEntity;
import com.ntunghoi.pizza.engine.entities.OrderItemEntity;
import com.ntunghoi.pizza.engine.models.Order;
import com.ntunghoi.pizza.engine.models.OrderItem;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class Entity2ModelUnitTest {
  private ModelMapper modelMapper = new ModelMapper();

  @Test
  public void whenConvertOrderItemEntityToOrderItem_thenCorrect() {
    String name = "name";
    int quantity = 3;
    double price = 3.3;

    OrderItemEntity entity = new OrderItemEntity();
    entity.setName(name);
    entity.setQuantity(quantity);
    entity.setPrice(price);

    OrderItem model = modelMapper.map(entity, OrderItem.class);
    assertEquals(model.getName(), name);
    assertEquals(model.getQuantity(), quantity);
    assertEquals(model.getPrice(), price);
  }

  @Test
  public void whenConvertOrderEntityToOrder_thenCorrect() {
    String name = "name";
    int quantity = 3;
    double price = 3.3;

    OrderItemEntity itemEntity = new OrderItemEntity();
    itemEntity.setName(name);
    itemEntity.setQuantity(quantity);
    itemEntity.setPrice(price);

    OrderEntity orderEntity = new OrderEntity();
    Set<OrderItemEntity> items = new HashSet<OrderItemEntity>() {
      {
        add(itemEntity);
      }
    };
    orderEntity.setItems(items);

    Order order = modelMapper.map(orderEntity, Order.class);
    assertEquals(order.getItems().size(), orderEntity.getItems().size());
    Optional<OrderItem> orderItem = order.getItems().stream().findFirst();
    assertTrue(orderItem.isPresent());
    orderItem.ifPresent(item -> {
      assertEquals(item.getName(), name);
      assertEquals(item.getQuantity(), quantity);
      assertEquals(item.getPrice(), price);
    });
  }
}
