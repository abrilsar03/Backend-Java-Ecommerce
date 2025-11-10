package com.ecommerce.api.dto.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class ProductBaseRequest {

    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    protected String title;

    @Size(max = 4000, message = "Description must be maximum 4000 characters")
    protected String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0")
    protected BigDecimal price;

    @Size(max = 2000, message = "Photo URL must be maximum 2000 characters")
    @org.hibernate.validator.constraints.URL(message = "Photo URL must be a valid URL")
    protected String photoUrl;

    @NotNull(message = "Tax is required")
    @DecimalMin(value = "0", message = "Tax must be greater than or equal to 0")
    protected BigDecimal tax;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    protected Integer stock;

    public ProductBaseRequest() {}

    public ProductBaseRequest(String title, String description, BigDecimal price, String photoUrl,
            BigDecimal tax, Integer stock) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.photoUrl = photoUrl;
        this.tax = tax;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Convierte el precio (BigDecimal) a cents (Integer) multiplicando por 100
     */
    public Integer getPriceCents() {
        if (price == null) {
            return null;
        }
        return price.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).intValue();
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
