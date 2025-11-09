package com.ecommerce.api.entities;

import com.ecommerce.api.enums.SystemParamType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "system_params")
public class SystemParamEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "key", nullable = false, unique = true, length = 120)
    private SystemParamType key;

    @Column(name = "value", nullable = false, columnDefinition = "text")
    private String value;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public SystemParamEntity(SystemParamType key, String value) {
        this.key = key;
        this.value = value;
        this.updatedAt = OffsetDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

    public SystemParamType getKey() {
        return key;
    }

    public void setKey(SystemParamType key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
