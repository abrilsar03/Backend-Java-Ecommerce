package com.ecommerce.api.dto.cart;

import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
public class CartTotalsResponse {
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    public CartTotalsResponse(BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

