package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.StoreEntity;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {
}
