package com.ecommerce.api.dto.tokenization;

import jakarta.validation.constraints.*;

public class TokenizeCardRequest {
    @NotBlank
    private String pan;
    @NotBlank
    private String cvv;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer expMonth;
    @NotNull
    @Min(2024)
    @Max(2100)
    private Integer expYear;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }
}
