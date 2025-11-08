package com.playground.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.api.entities.PermissionEntity;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    Optional<PermissionEntity> findByResourceAndAction(String resource, String action);
}
