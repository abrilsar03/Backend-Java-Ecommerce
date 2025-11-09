package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.CartEntity;
import com.ecommerce.api.enums.CartStatusType;
import java.util.UUID;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {


        @Query("SELECT cart FROM CartEntity cart LEFT JOIN FETCH cart.items WHERE cart.user.id = :userId")
        Optional<CartEntity> findByUserIdWithItems(@Param("userId") UUID userId);

        @Query("SELECT cart FROM CartEntity cart LEFT JOIN FETCH cart.items WHERE cart.user.id = :userId AND cart.status = :status")
        Optional<CartEntity> findByUserAndStatusWithItems(@Param("userId") UUID userId,
                        @Param("status") CartStatusType status);

        Optional<CartEntity> findByUserIdAndStatus(UUID userId, CartStatusType status);
}
