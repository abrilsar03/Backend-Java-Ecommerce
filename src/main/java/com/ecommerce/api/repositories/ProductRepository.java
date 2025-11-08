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

  @Query("""
        SELECT p FROM ProductEntity p
        JOIN p.categories c
        WHERE c.id = :categoryId
      """)
  Page<ProductEntity> findAllByCategory(@Param("categoryId") UUID categoryId, Pageable pageable);

  @Query("""
        SELECT p FROM ProductEntity p
        JOIN p.tags t
        WHERE t.id = :tagId
      """)
  Page<ProductEntity> findAllByTag(@Param("tagId") UUID tagId, Pageable pageable);
}
