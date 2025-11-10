package com.ecommerce.api.dto.orders;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CardTokenInfoResponse {
    private String brand;
    private String last4;
    private Integer expMonth;
    private Integer expYear;

    public CardTokenInfoResponse(String brand, String last4, Integer expMonth, Integer expYear) {
        this.brand = brand;
        this.last4 = last4;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }
}

