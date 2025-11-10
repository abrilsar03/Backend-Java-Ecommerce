package com.ecommerce.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardUtils Tests")
class CardUtilsTest {

    @Test
    @DisplayName("Should validate valid Luhn number")
    void testLuhnValid_ValidNumber() {
        // Arrange - 4111111111111111 is a valid Luhn number
        String validPan = "4111111111111111";

        // Act
        boolean result = CardUtils.luhnValid(validPan);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should reject invalid Luhn number")
    void testLuhnValid_InvalidNumber() {
        // Arrange
        String invalidPan = "1234567890123456";

        // Act
        boolean result = CardUtils.luhnValid(invalidPan);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should validate VISA card number")
    void testLuhnValid_VisaCard() {
        // Arrange - Valid VISA test number
        String visaPan = "4532015112830366";

        // Act
        boolean result = CardUtils.luhnValid(visaPan);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should validate Mastercard number")
    void testLuhnValid_Mastercard() {
        // Arrange - Valid Mastercard test number
        String mastercardPan = "5555555555554444";

        // Act
        boolean result = CardUtils.luhnValid(mastercardPan);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should generate HMAC SHA256 hex successfully")
    void testHmacSha256Hex_Success() {
        // Arrange
        String secret = "test-secret-key";
        String data = "test-data";

        // Act
        String result = CardUtils.hmacSha256Hex(secret, data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // HMAC SHA256 produces 64 hex characters
        assertEquals(64, result.length());
    }

    @Test
    @DisplayName("Should generate consistent HMAC for same input")
    void testHmacSha256Hex_Consistency() {
        // Arrange
        String secret = "test-secret-key";
        String data = "test-data";

        // Act
        String result1 = CardUtils.hmacSha256Hex(secret, data);
        String result2 = CardUtils.hmacSha256Hex(secret, data);

        // Assert
        assertEquals(result1, result2);
    }

    @Test
    @DisplayName("Should generate different HMAC for different data")
    void testHmacSha256Hex_DifferentData() {
        // Arrange
        String secret = "test-secret-key";
        String data1 = "test-data-1";
        String data2 = "test-data-2";

        // Act
        String result1 = CardUtils.hmacSha256Hex(secret, data1);
        String result2 = CardUtils.hmacSha256Hex(secret, data2);

        // Assert
        assertNotEquals(result1, result2);
    }

    @Test
    @DisplayName("Should generate different HMAC for different secrets")
    void testHmacSha256Hex_DifferentSecrets() {
        // Arrange
        String secret1 = "test-secret-key-1";
        String secret2 = "test-secret-key-2";
        String data = "test-data";

        // Act
        String result1 = CardUtils.hmacSha256Hex(secret1, data);
        String result2 = CardUtils.hmacSha256Hex(secret2, data);

        // Assert
        assertNotEquals(result1, result2);
    }
}

