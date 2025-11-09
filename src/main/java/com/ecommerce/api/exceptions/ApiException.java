package com.ecommerce.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import com.ecommerce.api.enums.ErrorCode;
import java.time.LocalDateTime;

@Getter
public class ApiException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;
    private final LocalDateTime timestamp;
    private final String details;

    public ApiException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), null);
    }

    public ApiException(ErrorCode errorCode, String details) {
        this(errorCode, errorCode.getDefaultMessage(), details);
    }

    public ApiException(ErrorCode errorCode, String customMessage, String details) {
        super(customMessage);
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getHttpStatus();
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }
}
