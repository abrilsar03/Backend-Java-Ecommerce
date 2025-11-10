package com.ecommerce.api.dto.orders;

import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public class PaymentResponse {
    private UUID id;
    private String status;
    private String paymentType;
    private Integer attempts;
    private String reference;
    private CardTokenInfoResponse cardToken;

    public PaymentResponse(UUID id, String status, String paymentType, Integer attempts,
            String reference, CardTokenInfoResponse cardToken) {
        this.id = id;
        this.status = status;
        this.paymentType = paymentType;
        this.attempts = attempts;
        this.reference = reference;
        this.cardToken = cardToken;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public CardTokenInfoResponse getCardToken() {
        return cardToken;
    }

    public void setCardToken(CardTokenInfoResponse cardToken) {
        this.cardToken = cardToken;
    }
}

