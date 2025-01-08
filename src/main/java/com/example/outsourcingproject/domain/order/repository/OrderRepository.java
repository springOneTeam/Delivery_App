package com.example.outsourcingproject.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
