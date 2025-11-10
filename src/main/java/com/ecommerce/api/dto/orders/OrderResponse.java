package com.ecommerce.api.dto.orders;

import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class OrderResponse {
    private UUID id;
    private UUID userId;
    private String userEmail;
    private String status;
    private String shippingAddress;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private List<OrderItemResponse> items;
    private PaymentResponse payment;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public OrderResponse(UUID id, UUID userId, String userEmail, String status,
            String shippingAddress, BigDecimal subtotal, BigDecimal tax, BigDecimal total,
            List<OrderItemResponse> items, PaymentResponse payment, OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.items = items;
        this.payment = payment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Convierte el subtotal de cents (Integer) a BigDecimal dividiendo por 100
     */
    public void setSubtotalFromCents(Integer subtotalCents) {
        if (subtotalCents == null) {
            this.subtotal = null;
        } else {
            this.subtotal = new BigDecimal(subtotalCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * Convierte el tax de cents (Integer) a BigDecimal dividiendo por 100
     */
    public void setTaxFromCents(Integer taxCents) {
        if (taxCents == null) {
            this.tax = null;
        } else {
            this.tax = new BigDecimal(taxCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Convierte el total de cents (Integer) a BigDecimal dividiendo por 100
     */
    public void setTotalFromCents(Integer totalCents) {
        if (totalCents == null) {
            this.total = null;
        } else {
            this.total = new BigDecimal(totalCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
