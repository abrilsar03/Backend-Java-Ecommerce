package com.ecommerce.api.dto.apiKeys;

import com.ecommerce.api.entities.BasicEntity;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ApiKeyResponse extends BasicEntity {

    private String name;
    private String key;

    public ApiKeyResponse(UUID id, String name, String key, Boolean active,
            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super();
        this.setId(id);
        this.setActive(active);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.name = name;
        this.key = key;
    }



    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

}

