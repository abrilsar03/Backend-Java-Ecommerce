package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import com.ecommerce.api.enums.RoleCodeType;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleEntity extends BasicEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleCodeType role;

    @Column(nullable = false)
    @Size(min = 2, message = "El estado debe tener al menos 2 caracteres")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions = new HashSet<>();


    public RoleCodeType getRole() {
        return role;
    }

    public void setRole(RoleCodeType role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }
}

