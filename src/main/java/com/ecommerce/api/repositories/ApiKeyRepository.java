package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, UUID> {
    Optional<ApiKeyEntity> findByKeyAndActiveTrue(String key);

    Optional<ApiKeyEntity> findByNameAndActiveTrue(String name);
}
