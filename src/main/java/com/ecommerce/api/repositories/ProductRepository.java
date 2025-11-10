package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.ProductEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface ProductRepository
    extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

  Optional<ProductEntity> findBySku(String sku);

}
