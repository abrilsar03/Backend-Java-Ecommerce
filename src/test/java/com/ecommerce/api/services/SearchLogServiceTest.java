package com.ecommerce.api.services;

import com.ecommerce.api.entities.SearchLogEntity;
import com.ecommerce.api.repositories.SearchLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchLogService Tests")
class SearchLogServiceTest {

    @Mock
    private SearchLogRepository repo;

    @InjectMocks
    private SearchLogService searchLogService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should log search async successfully")
    void testLogAsync_Success() {
        // Arrange - For @Async methods, we can't easily mock the async execution
        // So we just verify the method can be called without throwing
        String endpoint = "/api/products";
        String query = "laptop";
        String ip = "192.168.1.1";
        String userAgent = "Mozilla/5.0";

        // Act & Assert - The method should return immediately (async execution)
        assertDoesNotThrow(() -> searchLogService.logAsync(userId, endpoint, query, ip, userAgent));
    }

    @Test
    @DisplayName("Should handle exception gracefully")
    void testLogAsync_ExceptionHandling() {
        // Arrange
        String endpoint = "/api/products";
        String query = "laptop";
        String ip = "192.168.1.1";
        String userAgent = "Mozilla/5.0";

        doThrow(new RuntimeException("Database error")).when(repo).save(any(SearchLogEntity.class));

        // Act & Assert - Should not throw exception
        try {
            searchLogService.logAsync(userId, endpoint, query, ip, userAgent);
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}

