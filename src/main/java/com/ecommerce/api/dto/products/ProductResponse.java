package com.ecommerce.api.dto.products;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String title;
    private String sku;
    private String description;
    private BigDecimal price;
    private String photoUrl;
    private BigDecimal tax;
    private Boolean active;
    private Integer stock;


    public ProductResponse(UUID id, String title, String sku, String description,
            BigDecimal price, String photoUrl, BigDecimal tax, Boolean active, Integer stock) {
        this.id = id;
        this.title = title;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.photoUrl = photoUrl;
        this.tax = tax;
        this.active = active;
        this.stock = stock;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Convierte el precio de cents (Integer) a BigDecimal dividiendo por 100
     */
    public void setPriceFromCents(Integer priceCents) {
        if (priceCents == null) {
            this.price = null;
        } else {
            this.price = new BigDecimal(priceCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
