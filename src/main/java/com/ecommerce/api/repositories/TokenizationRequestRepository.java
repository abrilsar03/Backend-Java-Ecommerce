// repositories/TokenizationRequestRepository.java
package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.TokenizationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenizationRequestRepository
        extends JpaRepository<TokenizationRequestEntity, UUID> {
}
