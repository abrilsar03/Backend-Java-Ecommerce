package com.ecommerce.api.keys;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class OrderItemPk implements Serializable {
    private UUID order;
    private UUID product;

    public OrderItemPk() {}

    public OrderItemPk(UUID order, UUID product) {
        this.order = order;
        this.product = product;
    }

    public UUID getOrder() {
        return order;
    }

    public void setOrder(UUID order) {
        this.order = order;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(UUID product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderItemPk that))
            return false;
        return Objects.equals(order, that.order) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
