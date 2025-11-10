package com.ecommerce.api.entities;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "products")
public class ProductEntity extends BasicEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "sku", nullable = false, unique = true, length = 255)
    private String sku;

    @Size(max = 4000)
    @Column(name = "description", length = 4000)
    private String description;

    @Min(0)
    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;

    @Size(max = 2000)
    @Column(name = "photo_url", length = 2000)
    private String photoUrl;

    @Column(name = "tax", nullable = false, precision = 20, scale = 8)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "active", nullable = false)
    private Boolean active = true;


    @Min(0)
    @Column(name = "stock")
    private Integer stock;

    public ProductEntity() {}

    public ProductEntity(String title, String sku, String description, Integer priceCents,
            String photoUrl, BigDecimal tax, Integer stock) {
        super();
        this.title = title;
        this.sku = sku;
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
