package com.ecommerce.api.utils;

import com.ecommerce.api.model.AuthUser;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthUserJwtUtils Tests")
class AuthUserJwtUtilsTest {

    @InjectMocks
    private AuthUserJwtUtils authUserJwtUtils;

    private Claims claims;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        claims = mock(Claims.class);
        
        // Set up JWT secret and expiration using reflection
        ReflectionTestUtils.setField(authUserJwtUtils, "jwtSecret", "test-secret-key-that-is-long-enough-for-hmac-sha256");
        ReflectionTestUtils.setField(authUserJwtUtils, "jwtExpiration", 3600L);
        
        // Initialize the jwtKey
        authUserJwtUtils.init();
    }

    @Test
    @DisplayName("Should create AuthUser from claims successfully")
    void testCreatePayloadInstance_Success() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", userId.toString());
        claimsMap.put("userId", userId.toString());
        claimsMap.put("email", "test@example.com");
        claimsMap.put("firstName", "John");
        claimsMap.put("lastName", "Doe");

        lenient().when(claims.get("sub")).thenReturn(userId.toString());
        lenient().when(claims.get("userId")).thenReturn(userId.toString());
        lenient().when(claims.get("email")).thenReturn("test@example.com");
        lenient().when(claims.get("firstName")).thenReturn("John");
        lenient().when(claims.get("lastName")).thenReturn("Doe");
        lenient().when(claims.entrySet()).thenReturn(claimsMap.entrySet());

        // Act
        AuthUser result = authUserJwtUtils.createPayloadInstance(claims);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    @DisplayName("Should handle claims with userId instead of sub")
    void testCreatePayloadInstance_WithUserId() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("userId", userId.toString());
        claimsMap.put("email", "test@example.com");

        lenient().when(claims.get("sub")).thenReturn(null);
        lenient().when(claims.get("userId")).thenReturn(userId.toString());
        lenient().when(claims.get("email")).thenReturn("test@example.com");
        lenient().when(claims.entrySet()).thenReturn(claimsMap.entrySet());

        // Act
        AuthUser result = authUserJwtUtils.createPayloadInstance(claims);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    @DisplayName("Should handle missing user ID in claims")
    void testCreatePayloadInstance_MissingUserId() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("email", "test@example.com");

        lenient().when(claims.get("sub")).thenReturn(null);
        lenient().when(claims.get("userId")).thenReturn(null);
        lenient().when(claims.get("email")).thenReturn("test@example.com");
        lenient().when(claims.entrySet()).thenReturn(claimsMap.entrySet());

        // Act
        AuthUser result = authUserJwtUtils.createPayloadInstance(claims);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
    }
}

