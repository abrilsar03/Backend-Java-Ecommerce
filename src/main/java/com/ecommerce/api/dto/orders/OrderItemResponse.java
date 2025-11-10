package com.ecommerce.api.dto.orders;

import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@NoArgsConstructor
public class OrderItemResponse {
    private UUID id;
    private UUID productId;
    private String nameSnapshot;
    private String skuSnapshot;
    private BigDecimal price;
    private BigDecimal taxPercent;
    private Integer quantity;
    private BigDecimal lineTotal;

    public OrderItemResponse(UUID id, UUID productId, String nameSnapshot, String skuSnapshot,
            BigDecimal price, BigDecimal taxPercent, Integer quantity, BigDecimal lineTotal) {
        this.id = id;
        this.productId = productId;
        this.nameSnapshot = nameSnapshot;
        this.skuSnapshot = skuSnapshot;
        this.price = price;
        this.taxPercent = taxPercent;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getNameSnapshot() {
        return nameSnapshot;
    }

    public void setNameSnapshot(String nameSnapshot) {
        this.nameSnapshot = nameSnapshot;
    }

    public String getSkuSnapshot() {
        return skuSnapshot;
    }

    public void setSkuSnapshot(String skuSnapshot) {
        this.skuSnapshot = skuSnapshot;
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

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    /**
     * Convierte el lineTotal de cents (Integer) a BigDecimal dividiendo por 100
     */
    public void setLineTotalFromCents(Integer lineTotalCents) {
        if (lineTotalCents == null) {
            this.lineTotal = null;
        } else {
            this.lineTotal = new BigDecimal(lineTotalCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
    }
}

