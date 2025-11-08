package com.ecommerce.api.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries",
        indexes = {@Index(name = "idx_country_iso", columnList = "iso", unique = true)})

public class CountryEntity extends BasicEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 3, nullable = false, unique = true)
    private String iso;

    @ManyToOne
    @JoinColumn(name = "default_currency_id")
    private CurrencyEntity defaultCurrency;

    @ManyToMany
    @JoinTable(name = "country_currencies", joinColumns = @JoinColumn(name = "country_id"),
            inverseJoinColumns = @JoinColumn(name = "currency_id"))
    private Set<CurrencyEntity> supportedCurrencies = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public CurrencyEntity getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(CurrencyEntity defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Set<CurrencyEntity> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public void setSupportedCurrencies(Set<CurrencyEntity> supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
    }

}

