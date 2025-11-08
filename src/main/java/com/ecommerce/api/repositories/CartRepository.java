package com.ecommerce.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.cart.CartEntity;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(UUID userId);
}
