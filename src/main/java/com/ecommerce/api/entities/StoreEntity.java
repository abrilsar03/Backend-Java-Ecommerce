package com.ecommerce.api.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "stores")
public class StoreEntity extends BasicEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @Column(name = "phone_code", length = 5)
    private String phoneCode;

    @Column(name = "phone", length = 20)
    private String phone;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }
}

