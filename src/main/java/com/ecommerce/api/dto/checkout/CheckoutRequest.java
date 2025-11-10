package com.ecommerce.api.dto.checkout;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {
    @NotBlank
    private String cardToken;
    @NotBlank
    private String shippingAddress;

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
