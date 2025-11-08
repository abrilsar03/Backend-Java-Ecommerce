package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.cart.CartItemEntity;
import com.ecommerce.api.entities.cart.CartItemId;
import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemId> {
    List<CartItemEntity> findByCart_UserId(UUID userId);

    void deleteByCart_UserIdAndProduct_Id(UUID userId, UUID productId);
}
