package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.TagEntity;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {
    Optional<TagEntity> findByNameIgnoreCase(String name);
}
