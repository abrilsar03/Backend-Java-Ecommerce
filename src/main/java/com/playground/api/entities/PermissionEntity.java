package com.playground.api.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(name = "unique_permission",
        columnNames = {"resource", "action"}))

public class PermissionEntity extends BasicEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionResource resource;

    public enum PermissionResource {
        PRODUCT, USER, ORDER
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionAction action;

    public enum PermissionAction {
        READ, WRITE, DELETE, LIST
    }

    public PermissionResource getResource() {
        return resource;
    }

    public void setResource(PermissionResource resource) {
        this.resource = resource;
    }

    public PermissionAction getAction() {
        return action;
    }

    public void setAction(PermissionAction action) {
        this.action = action;
    }


}

