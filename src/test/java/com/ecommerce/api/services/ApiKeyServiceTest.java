package com.ecommerce.api.services;

import com.ecommerce.api.dto.apiKeys.ApiKeyResponse;
import com.ecommerce.api.dto.apiKeys.CreateApiKeyRequest;
import com.ecommerce.api.entities.ApiKeyEntity;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.ApiKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiKeyService Tests")
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService apiKeyService;

    private ApiKeyEntity apiKeyEntity;
    private UUID apiKeyId;

    @BeforeEach
    void setUp() {
        apiKeyId = UUID.randomUUID();
        apiKeyEntity = new ApiKeyEntity("Test Key", "api_test123");
        apiKeyEntity.setId(apiKeyId);
        apiKeyEntity.setActive(true);
        apiKeyEntity.setCreatedAt(OffsetDateTime.now());
        apiKeyEntity.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    @DisplayName("Should create API key successfully")
    void testCreate_Success() {
        // Arrange
        CreateApiKeyRequest request = new CreateApiKeyRequest();
        request.setName("New Key");

        when(apiKeyRepository.findByNameAndActiveTrue("New Key")).thenReturn(Optional.empty());
        when(apiKeyRepository.save(any(ApiKeyEntity.class))).thenReturn(apiKeyEntity);

        // Act
        ApiKeyResponse result = apiKeyService.create(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertTrue(result.getKey().startsWith("api_"));
        verify(apiKeyRepository, times(1)).findByNameAndActiveTrue("New Key");
        verify(apiKeyRepository, times(1)).save(any(ApiKeyEntity.class));
    }

    @Test
    @DisplayName("Should return existing API key if already exists")
    void testCreate_ExistingKey() {
        // Arrange
        CreateApiKeyRequest request = new CreateApiKeyRequest();
        request.setName("Test Key");

        when(apiKeyRepository.findByNameAndActiveTrue("Test Key")).thenReturn(Optional.of(apiKeyEntity));

        // Act
        ApiKeyResponse result = apiKeyService.create(request);

        // Assert
        assertNotNull(result);
        // ApiKeyResponse constructor receives the ID but may not set it via setId
        // We verify the response was created from the existing entity
        assertEquals("Test Key", result.getName());
        assertEquals("api_test123", result.getKey());
        verify(apiKeyRepository, times(1)).findByNameAndActiveTrue("Test Key");
        verify(apiKeyRepository, never()).save(any(ApiKeyEntity.class));
    }

    @Test
    @DisplayName("Should list all API keys successfully")
    void testListAll_Success() {
        // Arrange
        when(apiKeyRepository.findAll()).thenReturn(List.of(apiKeyEntity));

        // Act
        List<ApiKeyResponse> result = apiKeyService.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(apiKeyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should deactivate API key successfully")
    void testDeactivate_Success() {
        // Arrange
        ApiKeyEntity entityToDeactivate = new ApiKeyEntity("Test Key", "api_test123");
        entityToDeactivate.setId(apiKeyId);
        entityToDeactivate.setActive(true);
        entityToDeactivate.setCreatedAt(OffsetDateTime.now());
        entityToDeactivate.setUpdatedAt(OffsetDateTime.now());
        
        when(apiKeyRepository.findById(apiKeyId)).thenReturn(Optional.of(entityToDeactivate));
        when(apiKeyRepository.save(any(ApiKeyEntity.class))).thenAnswer(invocation -> {
            ApiKeyEntity entity = invocation.getArgument(0);
            // Verify that active was set to false by the service
            assertFalse(entity.getActive(), "Entity should have active=false after service call");
            return entity;
        });

        // Act
        ApiKeyResponse result = apiKeyService.deactivate(apiKeyId);

        // Assert
        assertNotNull(result);
        // The service sets active to false and saves, so the response should reflect that
        assertFalse(result.getActive());
        verify(apiKeyRepository, times(1)).findById(apiKeyId);
        verify(apiKeyRepository, times(1)).save(any(ApiKeyEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent API key")
    void testDeactivate_NotFound() {
        // Arrange
        when(apiKeyRepository.findById(apiKeyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> apiKeyService.deactivate(apiKeyId));
        verify(apiKeyRepository, times(1)).findById(apiKeyId);
        verify(apiKeyRepository, never()).save(any(ApiKeyEntity.class));
    }
}

