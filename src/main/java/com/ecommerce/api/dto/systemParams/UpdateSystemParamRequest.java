package com.ecommerce.api.dto.systemParams;

import jakarta.validation.constraints.NotBlank;

public class UpdateSystemParamRequest {
    @NotBlank
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
