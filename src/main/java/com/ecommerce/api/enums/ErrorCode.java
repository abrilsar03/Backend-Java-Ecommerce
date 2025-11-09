package com.ecommerce.api.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    AUTH_INVALID_CREDENTIALS("401", "Invalid credentials"),

    AUTH_TOKEN_EXPIRED("401", "Token expired"),

    AUTH_TOKEN_INVALID("401", "Token invalid"),

    AUTH_UNAUTHORIZED("401", "Unauthorized access"),

    USER_NOT_FOUND("404", "User not found"),


    USER_ALREADY_EXISTS("400", "User already exists"),

    USER_INVALID_DATA("400", "Invalid user data"),

    PRODUCT_NOT_FOUND("404", "Product not found"),

    PRODUCT_OUT_OF_STOCK("400", "Product out of stock"),

    PRODUCT_INVALID_PRICE("400", "Invalid product price"),

    ORDER_NOT_FOUND("404", "Order not found"),

    ORDER_INVALID_STATUS("400", "Invalid order status"),

    INTERNAL_ERROR("500", "Internal server error"),

    VALIDATION_ERROR("400", "Validation failed"),

    ROLE_NOT_FOUND("404", "Role not found"),

    EMAIL_ALREADY_EXISTS("400", "Email already exists"),

    SYSTEM_PARAM_EXISTS("409", "System parameter already exists"),

    SKU_EXISTS("409", "Sku already exists"),

    SYSTEM_PARAM_NOT_FOUND("404", "System parameter not found");



    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(Integer.parseInt(code.split("-")[0]));
    }
}
