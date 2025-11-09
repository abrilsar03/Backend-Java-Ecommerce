
package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.CartItemEntity;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
    Optional<CartItemEntity> findByCart_IdAndProduct_Id(UUID cartId, UUID productId);

    void deleteByCart_IdAndProduct_Id(UUID cartId, UUID productId);
}


