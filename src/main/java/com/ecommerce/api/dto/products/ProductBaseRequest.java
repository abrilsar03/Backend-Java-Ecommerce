package com.ecommerce.api.dto.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.springframework.security.access.method.P;

public abstract class ProductBaseRequest {

    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    protected String title;

    @Size(max = 4000, message = "Description must be maximum 4000 characters")
    protected String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    protected Integer priceCents;

    @Size(max = 2000, message = "Photo URL must be maximum 2000 characters")
    @org.hibernate.validator.constraints.URL(message = "Photo URL must be a valid URL")
    protected String photoUrl;

    @DecimalMin(value = "0", message = "Tax must be greater than or equal to 0")
    protected BigDecimal tax;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    protected Integer stock;

    public ProductBaseRequest() {}

    public ProductBaseRequest(String title, String description, Integer priceCents, String photoUrl,
            BigDecimal tax, Integer stock) {
        this.title = title;
        this.description = description;
        this.priceCents = priceCents;
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
