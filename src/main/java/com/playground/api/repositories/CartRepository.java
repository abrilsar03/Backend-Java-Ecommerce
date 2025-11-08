package com.playground.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.api.entities.cart.CartEntity;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(UUID userId);
}
