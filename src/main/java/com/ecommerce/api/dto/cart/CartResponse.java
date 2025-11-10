package com.ecommerce.api.dto.cart;

import java.util.List;
import java.util.UUID;
import com.ecommerce.api.enums.CartStatusType;

public class CartResponse {
    private UUID cartId;
    private UUID userId;
    private CartStatusType status;
    private List<CartItemResponse> items;

    public CartResponse() {}

    public CartResponse(UUID cartId, UUID userId, CartStatusType status,
            List<CartItemResponse> items) {
        this.cartId = cartId;
        this.userId = userId;
        this.status = status;
        this.items = items;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CartStatusType getStatus() {
        return status;
    }

    public void setStatus(CartStatusType status) {
        this.status = status;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
}
