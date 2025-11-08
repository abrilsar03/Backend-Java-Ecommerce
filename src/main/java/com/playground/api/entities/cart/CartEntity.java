package com.playground.api.entities.cart;


import jakarta.persistence.*;

import java.util.UUID;
import com.playground.api.entities.BasicEntity;
import com.playground.api.entities.StoreEntity;

@Entity
@Table(name = "carts")
public class CartEntity extends BasicEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

}

