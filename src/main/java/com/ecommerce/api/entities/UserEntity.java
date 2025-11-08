package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "username", unique = true, length = 50)
    private String username;

    @Column(name = "phone_code", length = 5)
    private String phoneCode;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "document_number", nullable = false, length = 20)
    private String documentNumber;

    @Column(name = "document_type", length = 10)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}

