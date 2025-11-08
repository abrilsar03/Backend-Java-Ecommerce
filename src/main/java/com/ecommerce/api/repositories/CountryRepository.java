package com.ecommerce.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.CountryEntity;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
    Optional<CountryEntity> findByIso(String iso);

    Optional<CountryEntity> findByName(String name);
}
