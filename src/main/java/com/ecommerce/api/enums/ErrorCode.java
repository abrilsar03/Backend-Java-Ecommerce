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

    CART_NOT_FOUND("404", "Cart not found"),

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

    MISSING_DATA("400", "Missing parameter"),


    SYSTEM_PARAM_NOT_FOUND("404", "System parameter not found"),

    INSUFFICIENT_STOCK("422", "Insufficient stock available"),

    TOKENIZATION_REJECTED("422", "It was not possible to tokenize the card"), API_KEY_NOT_FOUND(
            "404", "API key not found"),

    DISABLE_PRODUCT("400", "The product is not available at the moment"),

    CART_NOT_FOUND_ERROR("404", "Active cart not found"),

    CART_WITHOUT_ITEMS("400", "Cart is empty"),

    INVALID_QUANTITY("400", "Invalid quantity"),

    PRODUCT_INACTIVE("400", "Product is inactive"),

    ORDER_INVALID_STATUS_FOR_PAYMENT("400", "Order cannot be paid in current status");

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
