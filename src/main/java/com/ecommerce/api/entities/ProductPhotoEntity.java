package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "product_photos")

public class ProductPhotoEntity extends BasicEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false)
    private String url;

    @Min(0)
    @Column(nullable = false)
    private Integer position = 0;


    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}

