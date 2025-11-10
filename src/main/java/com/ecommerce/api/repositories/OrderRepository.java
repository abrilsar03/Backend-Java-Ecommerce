package com.ecommerce.api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.OrderEntity;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("SELECT o FROM OrderEntity o " +
           "LEFT JOIN FETCH o.items " +
           "LEFT JOIN FETCH o.user " +
           "WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItemsAndUser(@Param("id") UUID id);
}
