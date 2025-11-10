package com.ecommerce.api.dto.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartItemResponse {
    private UUID productId;
    private String title;
    private String sku;
    private BigDecimal price;
    private Integer quantity;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
