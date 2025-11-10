package com.ecommerce.api.dto.users;

import com.ecommerce.api.enums.DocumentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public abstract class UserBaseRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s'-]+$",
            message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    protected String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s'-]+$",
            message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    protected String lastName;

    @Size(max = 5, message = "Phone code must be maximum 5 characters")
    @Pattern(regexp = "^\\+?[0-9]{1,4}$",
            message = "Phone code must contain only numbers and optional + sign (1-4 digits)")
    protected String phoneCode;

    @Size(max = 15, message = "Phone number must be maximum 15 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers")
    protected String phone;

    @Size(max = 200, message = "Address must be maximum 200 characters")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-\\,\\.\\#\\/]+$",
            message = "Address can only contain letters, numbers, spaces, and basic punctuation")
    protected String address;

    protected DocumentType documentType;

    @Size(min = 5, max = 20, message = "Document number must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$",
            message = "Document number must contain only letters and numbers")
    protected String documentNumber;

    public UserBaseRequest() {}

    public UserBaseRequest(String firstName, String lastName, String phoneCode, String phone,
            String address, DocumentType documentType, String documentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneCode = phoneCode;
        this.phone = phone;
        this.address = address;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }

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

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
