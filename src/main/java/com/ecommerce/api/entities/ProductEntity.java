package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "products")
public class ProductEntity extends BasicEntity {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255, min = 3)
    private String sku;

    @Size(max = 4000)
    private String description;

    @Min(0)
    private Integer price_cents;

    @Column(name = "photo_url")
    @NotBlank
    @Size(max = 2000)
    private String photoUrl;

    @Min(0)
    private Double tax;

    @Min(0)
    @Column(name = "stock")
    @NotBlank
    private Integer stock;

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
        return price_cents;
    }

    public void setPriceCents(Integer price_cents) {
        this.price_cents = price_cents;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}

