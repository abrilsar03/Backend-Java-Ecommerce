package com.ecommerce.api.dto.products;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuery {
    private String name;
    private String sku;
    private Boolean hasStock;
    private Integer minPrice;
    private Integer maxPrice;
    private int page = 1;
    private int size = 10;
}
