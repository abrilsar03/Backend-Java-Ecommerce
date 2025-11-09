
package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.CartItemEntity;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
    Optional<CartItemEntity> findByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID cartId, UUID productId);
}


