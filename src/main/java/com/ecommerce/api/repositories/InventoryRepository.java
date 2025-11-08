package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.inventory.InventoryEntity;
import com.ecommerce.api.entities.inventory.InventoryId;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId> {
    List<InventoryEntity> findByStore_Id(java.util.UUID storeId);

    List<InventoryEntity> findByProduct_Id(java.util.UUID productId);

    boolean existsByStore_IdAndProduct_Id(java.util.UUID storeId, java.util.UUID productId);

    java.util.Optional<InventoryEntity> findByStore_IdAndProduct_Id(java.util.UUID storeId,
            java.util.UUID productId);
}
