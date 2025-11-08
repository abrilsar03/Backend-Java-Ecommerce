package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.PermissionEntity;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    Optional<PermissionEntity> findByResourceAndAction(String resource, String action);
}
