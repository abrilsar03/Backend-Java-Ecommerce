package com.ecommerce.api.dto.auth;

import com.ecommerce.api.dto.users.UserBaseRequest;
import com.ecommerce.api.enums.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterUserRequest extends UserBaseRequest {

    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email is required")
    @Size(min = 8, message = "Email must be at least 8 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;

    @NotBlank(message = "First name is required")
    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @NotBlank(message = "Last name is required")
    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @NotBlank(message = "Phone code is required")
    @Override
    public String getPhoneCode() {
        return super.getPhoneCode();
    }

    @NotBlank(message = "Phone number is required")
    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @NotBlank(message = "Address is required")
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @NotNull(message = "Document type is required")
    @Override
    public DocumentType getDocumentType() {
        return super.getDocumentType();
    }

    @NotBlank(message = "Document number is required")
    @Override
    public String getDocumentNumber() {
        return super.getDocumentNumber();
    }

    public RegisterUserRequest() {}

    public RegisterUserRequest(String email, String password, String firstName, String lastName,
            String address, String phoneCode, String phone, DocumentType documentType,
            String documentNumber) {
        super(firstName, lastName, address, phoneCode, phone, documentType, documentNumber);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
