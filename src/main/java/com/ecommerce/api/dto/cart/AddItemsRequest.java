package com.ecommerce.api.dto.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.checkerframework.checker.units.qual.A;

@NoArgsConstructor
public class AddItemsRequest {

    @NotEmpty(message = "Items list cannot be empty")
    @Size(max = 50, message = "Cannot add more than 50 items at once")
    @Valid
    private List<Item> items;


    public AddItemsRequest(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public AddItemsRequest() {}

    public AddItemsRequest(List<Item> items) {
        this.items = items;
    }

    public static class Item {

        @NotNull(message = "Product ID is required")
        private UUID productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 100, message = "Quantity cannot exceed 100")
        private Integer quantity;

        public Item() {}

        public Item(UUID productId, Integer quantity) {
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
}
