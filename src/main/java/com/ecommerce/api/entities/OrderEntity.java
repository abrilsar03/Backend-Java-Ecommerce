// src/main/java/com/ecommerce/api/entities/OrderEntity.java
package com.ecommerce.api.entities;

import com.ecommerce.api.enums.OrderStatusType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "orders", indexes = {@Index(name = "idx_orders_user_id", columnList = "user_id")})
public class OrderEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "subtotal_cents", nullable = false)
    private Integer subtotalCents;

    @Column(name = "tax_cents", nullable = false)
    private Integer taxCents = 0;

    @Column(name = "total_cents", nullable = false)
    private Integer totalCents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusType status;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<OrderItemEntity> items = new HashSet<>();

    public void addItem(OrderItemEntity item) {
        item.setOrder(this);
        this.items.add(item);
    }

    public void removeItem(OrderItemEntity item) {
        item.setOrder(null);
        this.items.remove(item);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getSubtotalCents() {
        return subtotalCents;
    }

    public void setSubtotalCents(Integer subtotalCents) {
        this.subtotalCents = subtotalCents;
    }

    public Integer getTaxCents() {
        return taxCents;
    }

    public void setTaxCents(Integer taxCents) {
        this.taxCents = taxCents;
    }

    public Integer getTotalCents() {
        return totalCents;
    }

    public void setTotalCents(Integer totalCents) {
        this.totalCents = totalCents;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Set<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemEntity> items) {
        this.items = items;
    }
}
