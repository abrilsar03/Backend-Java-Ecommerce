package com.ecommerce.api.dto.users;


import java.util.UUID;
import com.ecommerce.api.enums.DocumentType;

public class ProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneCode;
    private String phone;
    private String address;
    private String documentNumber;
    private DocumentType documentType;

    public ProfileResponse() {
        super();
    }

    public ProfileResponse(UUID id, String email, String firstName, String lastName,
            String phoneCode, String phone, String address, DocumentType documentType,
            String documentNumber) {
        super();
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneCode = phoneCode;
        this.phone = phone;
        this.address = address;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

}
