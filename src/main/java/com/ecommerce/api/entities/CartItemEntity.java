// src/main/java/com/ecommerce/api/entities/CartItemEntity.java
package com.ecommerce.api.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(name = "uq_cart_item_cart_product",
                columnNames = {"cart_id", "product_id"}),
        indexes = {@Index(name = "idx_cart_items_cart_id", columnList = "cart_id"),
                @Index(name = "idx_cart_items_product_id", columnList = "product_id")})


public class CartItemEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private int quantity;



    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
