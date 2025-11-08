package com.ecommerce.api.entities.cart;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Embeddable;

@Embeddable
public class CartItemId implements Serializable {
    private UUID userId;
    private UUID productId;

    public CartItemId() {}

    public CartItemId(UUID userId, UUID productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof CartItemId i))
            return false;

        return Objects.equals(userId, i.userId) && Objects.equals(productId, i.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}
