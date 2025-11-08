package com.playground.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.api.entities.ProductPhotoEntity;
import java.util.List;
import java.util.UUID;

public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, UUID> {
    List<ProductPhotoEntity> findByProduct_IdOrderByPositionAsc(UUID productId);
}
