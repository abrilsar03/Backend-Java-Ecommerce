package com.ecommerce.api.utils;

import org.springframework.data.jpa.domain.Specification;
import com.ecommerce.api.entities.ProductEntity;

public class ProductSpecifications {

    public static Specification<ProductEntity> nameContains(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("title")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> skuContains(String sku) {
        return (root, query, cb) -> sku == null ? null
                : cb.like(cb.lower(root.get("sku")), "%" + sku.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> hasStock(Boolean hasStock) {
        return (root, query, cb) -> hasStock == null ? null
                : hasStock ? cb.greaterThan(root.get("stock"), 0) : cb.equal(root.get("stock"), 0);
    }

    public static Specification<ProductEntity> priceAtLeast(Integer minPrice) {
        return (root, query, cb) -> minPrice == null ? null
                : cb.greaterThanOrEqualTo(root.get("priceCents"), minPrice);
    }

    public static Specification<ProductEntity> priceAtMost(Integer maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null
                : cb.lessThanOrEqualTo(root.get("priceCents"), maxPrice);
    }

    public static Specification<ProductEntity> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
}
