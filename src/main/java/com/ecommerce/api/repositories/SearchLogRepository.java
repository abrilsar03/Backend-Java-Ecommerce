package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.SearchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchLogRepository extends JpaRepository<SearchLogEntity, UUID> {
}
