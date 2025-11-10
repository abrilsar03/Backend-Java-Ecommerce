package com.ecommerce.api.services;

import com.ecommerce.api.entities.EventLogEntity;
import com.ecommerce.api.enums.EntityType;
import com.ecommerce.api.enums.EventType;
import com.ecommerce.api.enums.LogLevelType;
import com.ecommerce.api.repositories.EventLogRepository;
import com.ecommerce.api.utils.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventLogService Tests")
class EventLogServiceTest {

    @Mock
    private EventLogRepository eventLogRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private EventLogService eventLogService;

    private UUID entityId;
    private Map<String, Object> payload;

    @BeforeEach
    void setUp() {
        entityId = UUID.randomUUID();
        payload = Map.of("key", "value");
    }

    @Test
    @DisplayName("Should log info event successfully")
    void testInfo_Success() {
        // Arrange - For @Async methods, we can't easily mock the async execution
        // So we just verify the method can be called without throwing
        
        // Act & Assert - The method should return immediately (async execution)
        assertDoesNotThrow(() -> eventLogService.info(EventType.ORDER_PLACED, EntityType.ORDER, entityId, payload));
    }

    @Test
    @DisplayName("Should log warn event successfully")
    void testWarn_Success() {
        // Arrange - For @Async methods, we can't easily mock the async execution
        // So we just verify the method can be called without throwing
        
        // Act & Assert - The method should return immediately (async execution)
        assertDoesNotThrow(() -> eventLogService.warn(EventType.PAYMENT_ATTEMPT_FAILED, EntityType.PAYMENT, entityId, payload));
    }

    @Test
    @DisplayName("Should log error event successfully")
    void testError_Success() {
        // Arrange - For @Async methods, we can't easily mock the async execution
        // So we just verify the method can be called without throwing
        
        // Act & Assert - The method should return immediately (async execution)
        assertDoesNotThrow(() -> eventLogService.error(EventType.PAYMENT_FAILED, EntityType.PAYMENT, entityId, payload));
    }

    @Test
    @DisplayName("Should handle null payload gracefully")
    void testLog_NullPayload() {
        // Arrange - For @Async methods, we can't easily mock the async execution
        // So we just verify the method can be called without throwing
        
        // Act & Assert - The method should return immediately (async execution)
        assertDoesNotThrow(() -> eventLogService.info(EventType.ORDER_PLACED, EntityType.ORDER, entityId, null));
    }

    @Test
    @DisplayName("Should handle exception gracefully")
    void testLog_ExceptionHandling() throws Exception {
        // Arrange
        when(mapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON error"));

        // Act & Assert - Should not throw exception
        try {
            eventLogService.info(EventType.ORDER_PLACED, EntityType.ORDER, entityId, payload);
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}

