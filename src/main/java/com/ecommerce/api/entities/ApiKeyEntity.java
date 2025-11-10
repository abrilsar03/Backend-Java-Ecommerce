package com.ecommerce.api.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Table(name = "api_keys", indexes = @Index(name = "idx_api_keys_active", columnList = "active"))
public class ApiKeyEntity extends BasicEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "key", nullable = false, unique = true)
    private String key;



    public ApiKeyEntity(String name, String key) {
        super();
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

