package com.ntunghoi.pizza.engine.repositories;

import com.ntunghoi.pizza.engine.entities.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
