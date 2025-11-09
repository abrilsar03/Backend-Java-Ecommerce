package com.ecommerce.api.dto.products;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateProductRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(min = 3, max = 255)
    private String sku;

    @Size(max = 4000)
    private String description;

    @NotNull
    @Min(0)
    private Integer priceCents;

    @Size(max = 2000)
    private String photoUrl;

    @NotNull
    @DecimalMin("0")
    private BigDecimal tax;

    @Min(0)
    private Integer stock;

    // getters / setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
