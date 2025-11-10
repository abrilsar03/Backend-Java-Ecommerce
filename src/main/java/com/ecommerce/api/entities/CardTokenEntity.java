package com.ecommerce.api.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "card_tokens",
        indexes = {
                @Index(name = "idx_card_tokens_fingerprint", columnList = "fingerprint",
                        unique = true),
                @Index(name = "idx_card_tokens_token", columnList = "token", unique = true)})

public class CardTokenEntity extends BasicEntity {

    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false, unique = true)
    private String fingerprint;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false, length = 4)
    private String last4;
    @Column(name = "exp_month", nullable = false)
    private Short expMonth;
    @Column(name = "exp_year", nullable = false)
    private Short expYear;

    public CardTokenEntity(String token, String fingerprint, String brand, String last4,
            Short expMonth, Short expYear) {
        super();
        this.token = token;
        this.fingerprint = fingerprint;
        this.brand = brand;
        this.last4 = last4;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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

    public Short getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Short expMonth) {
        this.expMonth = expMonth;
    }

    public Short getExpYear() {
        return expYear;
    }

    public void setExpYear(Short expYear) {
        this.expYear = expYear;
    }
}

