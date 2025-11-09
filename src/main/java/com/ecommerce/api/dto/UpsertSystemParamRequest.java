package com.ecommerce.api.dto;

import com.ecommerce.api.enums.SystemParamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpsertSystemParamRequest {
    @NotNull
    private SystemParamType key;

    @NotBlank
    private String value;

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
}
