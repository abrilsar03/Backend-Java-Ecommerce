package com.playground.api.repositories;

import com.playground.api.entities.inventory.InventoryEntity;
import com.playground.api.entities.inventory.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId> {
    List<InventoryEntity> findByStore_Id(java.util.UUID storeId);

    List<InventoryEntity> findByProduct_Id(java.util.UUID productId);

    boolean existsByStore_IdAndProduct_Id(java.util.UUID storeId, java.util.UUID productId);

    java.util.Optional<InventoryEntity> findByStore_IdAndProduct_Id(java.util.UUID storeId,
            java.util.UUID productId);
}
