package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ecommerce.api.enums.DocumentType;

@Entity
@Table(name = "users")
public class UserEntity extends BasicEntity {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    @Email
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone_code", length = 5)
    private String phoneCode;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address")
    @Size(max = 255)
    private String address;

    @Column(name = "document_number", nullable = false, length = 20)
    private String documentNumber;

    @Column(name = "document_type", length = 10)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    public UserEntity() {
        super();
    }

    public UserEntity(String firstName, String lastName, String email, String password,
            PasswordEncoder passwordEncoder, String phoneCode, String phone, String address,
            String documentNumber, DocumentType documentType) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email != null ? email.trim().toLowerCase(Locale.ROOT) : null;
        this.phoneCode = phoneCode;
        this.phone = phone;
        this.address = address;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        setPassword(password, passwordEncoder);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> directPermissions = new HashSet<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<PermissionEntity> getDirectPermissions() {
        return directPermissions;
    }

    public void setDirectPermissions(Set<PermissionEntity> directPermissions) {
        this.directPermissions = directPermissions;
    }

    public void setPassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public String getPassword() {
        return password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}

