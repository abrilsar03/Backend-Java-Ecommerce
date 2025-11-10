package com.ecommerce.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RequestContext Tests")
class RequestContextTest {

    @Test
    @DisplayName("Should return null when no request context")
    void testCurrentRequestId_NoContext() {
        // Arrange - Clear any existing context
        RequestContextHolder.resetRequestAttributes();

        // Act
        UUID result = RequestContext.currentRequestId();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return request ID when present in request")
    void testCurrentRequestId_WithRequestId() {
        // Arrange
        UUID requestId = UUID.randomUUID();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("requestId", requestId);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        try {
            // Act
            UUID result = RequestContext.currentRequestId();

            // Assert
            assertNotNull(result);
            assertEquals(requestId, result);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    @DisplayName("Should return null when request ID is not UUID")
    void testCurrentRequestId_InvalidType() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("requestId", "not-a-uuid");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        try {
            // Act
            UUID result = RequestContext.currentRequestId();

            // Assert
            assertNull(result);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    @DisplayName("Should return null when request ID attribute is missing")
    void testCurrentRequestId_MissingAttribute() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        try {
            // Act
            UUID result = RequestContext.currentRequestId();

            // Assert
            assertNull(result);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}

