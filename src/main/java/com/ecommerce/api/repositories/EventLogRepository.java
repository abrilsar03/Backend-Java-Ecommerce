package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.EventLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventLogRepository extends JpaRepository<EventLogEntity, UUID> {
}
