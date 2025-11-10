package com.ecommerce.api.dto.cart;

import jakarta.validation.constraints.*;
import java.util.UUID;

public class UpdateItemRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer quantity;


    public UpdateItemRequest() {}

    public UpdateItemRequest(UUID productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
