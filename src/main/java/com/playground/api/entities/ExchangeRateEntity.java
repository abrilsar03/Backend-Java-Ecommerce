package com.playground.api.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exchange_rates")

public class ExchangeRateEntity extends BasicEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "base_currency_id")
    private CurrencyEntity baseCurrency;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quote_currency_id")
    private CurrencyEntity quoteCurrency;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal rate;

    @Column(name = "valid_from", nullable = false)
    private OffsetDateTime validFrom = OffsetDateTime.now();

    @Column(name = "valid_to")
    private OffsetDateTime validTo;

    @Column(nullable = false)
    private String source;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    public CurrencyEntity getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyEntity baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyEntity getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(CurrencyEntity quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public OffsetDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(OffsetDateTime validTo) {
        this.validTo = validTo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

}
