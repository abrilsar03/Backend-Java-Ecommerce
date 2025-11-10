package com.ecommerce.api.dto.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class CreateProductRequest extends ProductBaseRequest {

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 255, message = "SKU must be between 3 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$",
            message = "SKU can only contain letters, numbers, hyphens and underscores")
    private String sku;


    public CreateProductRequest() {}

    public CreateProductRequest(String title, String sku, String description, BigDecimal price,
            String photoUrl, BigDecimal tax, Integer stock) {
        super(title, description, price, photoUrl, tax, stock);
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @NotBlank(message = "Title is required")
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @NotNull(message = "Stock is required")
    @Override
    public Integer getStock() {
        return super.getStock();
    }
}
