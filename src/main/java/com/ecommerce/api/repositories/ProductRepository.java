package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.ProductEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

  Optional<ProductEntity> findBySku(String sku);

  @Query("""
          SELECT p FROM ProductEntity p
          WHERE p.active = true
            AND (p.stock IS NULL OR p.stock >= :minStock)
            AND (:q IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')))
      """)

  Page<ProductEntity> searchPublic(@Param("q") String query, @Param("minStock") int minStock,
      Pageable pageable);


  @Query("""
          SELECT p FROM ProductEntity p
          WHERE (:q IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')))
      """)
      
  Page<ProductEntity> searchAdmin(@Param("q") String query, Pageable pageable);
}
