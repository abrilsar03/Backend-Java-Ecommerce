package com.ecommerce.api.entities;

import com.ecommerce.api.enums.PermissionActionType;
import com.ecommerce.api.enums.PermissionResourceType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(name = "unique_permission",
        columnNames = {"resource", "action"}))

public class PermissionEntity extends BasicIdEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionResourceType resource;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionActionType action;


    public PermissionResourceType getResource() {
        return resource;
    }

    public void setResource(PermissionResourceType resource) {
        this.resource = resource;
    }

    public PermissionActionType getAction() {
        return action;
    }

    public void setAction(PermissionActionType action) {
        this.action = action;
    }


}

