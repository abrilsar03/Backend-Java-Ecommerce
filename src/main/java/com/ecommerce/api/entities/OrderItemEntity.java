// src/main/java/com/ecommerce/api/entities/OrderItemEntity.java
package com.ecommerce.api.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items",
        uniqueConstraints = @UniqueConstraint(name = "uq_order_item_order_product",
                columnNames = {"order_id", "product_id"}),
        indexes = {@Index(name = "idx_order_items_order_id", columnList = "order_id"),
                @Index(name = "idx_order_items_product_id", columnList = "product_id")})


public class OrderItemEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "name_snapshot", nullable = false)
    private String nameSnapshot;

    @Column(name = "sku_snapshot", nullable = false)
    private String skuSnapshot;

    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;

    @Column(name = "tax_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercent = BigDecimal.ZERO;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "line_total_cents", nullable = false)
    private Integer lineTotalCents;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
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

    public Integer getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public Integer getQty() {
        return quantity;
    }

    public void setQty(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLineTotalCents() {
        return lineTotalCents;
    }

    public void setLineTotalCents(Integer lineTotalCents) {
        this.lineTotalCents = lineTotalCents;
    }
}
