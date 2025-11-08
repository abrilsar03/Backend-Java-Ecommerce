package com.ecommerce.api.exceptions;

import lombok.Getter;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private String message;
    private int errorCode;
    private HttpStatus statusCode;

    public ApiException(String message) {
        super(message);
        this.errorCode = 200;
        this.statusCode = null;
    }
}
