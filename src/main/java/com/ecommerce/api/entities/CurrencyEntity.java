package com.ecommerce.api.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies", indexes = {@Index(name = "idx_currency_code", columnList = "code")})

public class CurrencyEntity extends BasicEntity {

    @Column(length = 3, nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "minor_unit", nullable = false)
    private Short minorUnit = 2;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getMinorUnit() {
        return minorUnit;
    }

    public void setMinorUnit(Short minorUnit) {
        this.minorUnit = minorUnit;
    }

}

