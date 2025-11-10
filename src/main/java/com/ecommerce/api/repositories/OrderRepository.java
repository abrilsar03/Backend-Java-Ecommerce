package com.ecommerce.api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
