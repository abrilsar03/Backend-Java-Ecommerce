package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
