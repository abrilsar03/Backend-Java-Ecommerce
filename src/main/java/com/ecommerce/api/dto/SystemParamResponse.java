package com.ecommerce.api.dto;

import com.ecommerce.api.enums.SystemParamType;
import java.time.OffsetDateTime;

public class SystemParamResponse {
    private SystemParamType key;
    private String value;
    private OffsetDateTime updatedAt;

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
