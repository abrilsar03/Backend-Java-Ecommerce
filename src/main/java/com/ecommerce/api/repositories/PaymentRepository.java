package com.ecommerce.api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.PaymentEntity;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {

    @Query("SELECT p FROM PaymentEntity p " +
           "LEFT JOIN FETCH p.cardToken " +
           "WHERE p.order.id = :orderId")
    Optional<PaymentEntity> findByOrderIdWithCardToken(@Param("orderId") UUID orderId);
}
