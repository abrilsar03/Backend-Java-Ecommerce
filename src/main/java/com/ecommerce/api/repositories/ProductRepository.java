package com.ecommerce.api.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.ProductEntity;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
  Optional<ProductEntity> findBySku(String sku);

  @Query("""
        SELECT p FROM ProductEntity p
        WHERE (:q IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')))
      """)
  Page<ProductEntity> searchByTitle(@Param("q") String query, Pageable pageable);

}
