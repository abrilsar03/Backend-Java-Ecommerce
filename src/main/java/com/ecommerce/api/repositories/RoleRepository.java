package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.RoleEntity;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByCode(String code);
}
