package com.ecommerce.api.utils;

import com.ecommerce.api.entities.ProductEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;

public class Validations {

    private void validateProductVisibility(ProductEntity product) {
        int minStock = systemParamsService.getAsInt(SystemParamType.min_stock_visibility, 15);

        if (Boolean.FALSE.equals(product.getActive())
                || (product.getStock() != null && product.getStock() < minStock)) {
            throw ExceptionFactory.productNotFound();
        }
    }
}
