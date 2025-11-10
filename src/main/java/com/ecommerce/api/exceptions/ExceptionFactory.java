package com.ecommerce.api.exceptions;

import com.ecommerce.api.enums.ErrorCode;

public class ExceptionFactory {

    public static ApiException invalidCredentials(String details) {
        return new ApiException(ErrorCode.AUTH_INVALID_CREDENTIALS, details);
    }

    public static ApiException tokenExpired() {
        return new ApiException(ErrorCode.AUTH_TOKEN_EXPIRED);
    }

    public static ApiException invalidToken() {
        return new ApiException(ErrorCode.AUTH_TOKEN_INVALID);
    }

    public static ApiException unauthorized(String message) {
        return new ApiException(ErrorCode.AUTH_UNAUTHORIZED, message);
    }

    public static ApiException roleNotFound(String details) {
        return new ApiException(ErrorCode.ROLE_NOT_FOUND, details);
    }

    public static ApiException systemParamNotFound() {
        return new ApiException(ErrorCode.SYSTEM_PARAM_NOT_FOUND);
    }

    public static ApiException emailAlreadyExist(String details) {
        return new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS, details);
    }

    public static ApiException userNotFound() {
        return new ApiException(ErrorCode.USER_NOT_FOUND);
    }

    public static ApiException systemParamAlreadyExists() {
        return new ApiException(ErrorCode.SYSTEM_PARAM_EXISTS);
    }

    public static ApiException skuAlreadyExists() {
        return new ApiException(ErrorCode.SKU_EXISTS);
    }

    public static ApiException productNotFound() {
        return new ApiException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    public static ApiException cartNotFound() {
        return new ApiException(ErrorCode.CART_NOT_FOUND);
    }

    public static ApiException missingData(String details) {
        return new ApiException(ErrorCode.MISSING_DATA, details);
    }

    public static ApiException insufficientStock() {
        return new ApiException(ErrorCode.INSUFFICIENT_STOCK);
    }

    public static ApiException tokenizationRejected() {
        return new ApiException(ErrorCode.TOKENIZATION_REJECTED);
    }

    public static ApiException apiKeyNotFound() {
        return new ApiException(ErrorCode.API_KEY_NOT_FOUND);
    }

    public static ApiException orderNotFound() {
        return new ApiException(ErrorCode.ORDER_NOT_FOUND);
    }

    public static ApiException cartNotFoundError() {
        return new ApiException(ErrorCode.CART_NOT_FOUND_ERROR);
    }

    public static ApiException cartWithoutItems() {
        return new ApiException(ErrorCode.CART_WITHOUT_ITEMS);
    }

    public static ApiException invalidQuantity() {
        return new ApiException(ErrorCode.INVALID_QUANTITY);
    }

    public static ApiException productInactive() {
        return new ApiException(ErrorCode.PRODUCT_INACTIVE);
    }

    public static ApiException orderInvalidStatusForPayment() {
        return new ApiException(ErrorCode.ORDER_INVALID_STATUS_FOR_PAYMENT);
    }
}
