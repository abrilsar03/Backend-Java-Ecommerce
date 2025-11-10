package com.ecommerce.api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
