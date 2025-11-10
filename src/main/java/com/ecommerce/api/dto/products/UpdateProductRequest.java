package com.ecommerce.api.dto.products;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateProductRequest extends ProductBaseRequest {


    private Boolean active = true;

    public UpdateProductRequest(String title, String description, BigDecimal price,
            String photoUrl, BigDecimal tax, Integer stock, Boolean active) {
        super(title, description, price, photoUrl, tax, stock);
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }
}
