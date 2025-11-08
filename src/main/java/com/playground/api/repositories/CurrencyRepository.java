package com.playground.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.api.entities.CurrencyEntity;
import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, UUID> {
    Optional<CurrencyEntity> findByCode(String code);

    Optional<CurrencyEntity> findByCodeIgnoreCase(String code);
}
