package com.ecommerce.api.dto.cart;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;

public class RemoveItemsRequest {
    @NotEmpty
    private List<UUID> productIds;

    public List<UUID> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<UUID> productIds) {
        this.productIds = productIds;
    }

}
