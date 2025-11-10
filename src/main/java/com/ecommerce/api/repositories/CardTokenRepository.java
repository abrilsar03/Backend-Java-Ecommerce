// repositories/CardTokenRepository.java
package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.CardTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardTokenRepository extends JpaRepository<CardTokenEntity, UUID> {
    Optional<CardTokenEntity> findByFingerprint(String fingerprint);

    Optional<CardTokenEntity> findByToken(String token);
}
