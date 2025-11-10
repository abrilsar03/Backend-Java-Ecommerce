package com.ecommerce.api.entities;

import com.ecommerce.api.enums.PaymentStatusType;
import com.ecommerce.api.enums.PaymentType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "payments")
public class PaymentEntity extends BasicEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatusType status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_token_id")
    private CardTokenEntity cardToken;

    @Column(nullable = false)
    private Integer attempts = 0;

    @Column
    private String reference;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public PaymentStatusType getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusType status) {
        this.status = status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public CardTokenEntity getCardToken() {
        return cardToken;
    }

    public void setCardToken(CardTokenEntity cardToken) {
        this.cardToken = cardToken;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}

