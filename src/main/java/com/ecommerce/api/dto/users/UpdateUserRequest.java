package com.ecommerce.api.dto.users;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(min = 1, max = 60)
    private String firstName;

    @Size(min = 1, max = 60)
    private String lastName;

    @Size(max = 5)
    private String phoneCode;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

