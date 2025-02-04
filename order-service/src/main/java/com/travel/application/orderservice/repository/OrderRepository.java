package com.travel.application.orderservice.repository;

import com.travel.application.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, String> {
}
