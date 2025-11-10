package com.ecommerce.api.entities;

import com.ecommerce.api.enums.CartStatusType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "carts", indexes = {@Index(name = "idx_carts_user_id", columnList = "user_id")})
public class CartEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatusType status = CartStatusType.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<CartItemEntity> items = new HashSet<>();



    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CartStatusType getStatus() {
        return status;
    }

    public void setStatus(CartStatusType status) {
        this.status = status;
    }

    public Set<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<CartItemEntity> items) {
        this.items = items;
    }

}
