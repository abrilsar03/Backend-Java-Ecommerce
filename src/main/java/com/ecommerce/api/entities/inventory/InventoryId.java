package com.ecommerce.api.entities.inventory;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class InventoryId implements Serializable {
    private UUID storeId;
    private UUID productId;

    public InventoryId() {}

    public InventoryId(UUID storeId, UUID productId) {
        this.storeId = storeId;
        this.productId = productId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
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

        if (!(o instanceof InventoryId that))
            return false;

        return Objects.equals(storeId, that.storeId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId);
    }
}

