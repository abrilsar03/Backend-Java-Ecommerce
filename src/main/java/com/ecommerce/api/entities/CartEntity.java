package com.ecommerce.api.entities;

import com.ecommerce.api.enums.CartStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts", indexes = {@Index(name = "idx_carts_user_id", columnList = "user_id")})
public class CartEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus status = CartStatus.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<CartItemEntity> items = new HashSet<>();



    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public Set<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<CartItemEntity> items) {
        this.items = items;
    }

}
