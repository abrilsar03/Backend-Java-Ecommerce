package com.ecommerce.api.services;

import com.ecommerce.api.dto.systemParams.SystemParamResponse;
import com.ecommerce.api.dto.systemParams.UpdateSystemParamRequest;
import com.ecommerce.api.dto.systemParams.UpsertSystemParamRequest;
import com.ecommerce.api.entities.SystemParamEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.SystemParamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SystemParamService Tests")
class SystemParamServiceTest {

    @Mock
    private SystemParamRepository systemParamsRepository;

    @InjectMocks
    private SystemParamService systemParamService;

    private SystemParamEntity systemParamEntity;

    @BeforeEach
    void setUp() {
        systemParamEntity = new SystemParamEntity(SystemParamType.min_stock_visibility, "15");
        systemParamEntity.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    @DisplayName("Should create system param successfully")
    void testCreate_Success() {
        // Arrange
        UpsertSystemParamRequest request = new UpsertSystemParamRequest();
        request.setKey(SystemParamType.min_stock_visibility);
        request.setValue("20");

        when(systemParamsRepository.existsById(SystemParamType.min_stock_visibility)).thenReturn(false);
        when(systemParamsRepository.save(any(SystemParamEntity.class))).thenReturn(systemParamEntity);

        // Act
        SystemParamResponse result = systemParamService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(SystemParamType.min_stock_visibility, result.getKey());
        verify(systemParamsRepository, times(1)).existsById(SystemParamType.min_stock_visibility);
        verify(systemParamsRepository, times(1)).save(any(SystemParamEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when system param already exists")
    void testCreate_AlreadyExists() {
        // Arrange
        UpsertSystemParamRequest request = new UpsertSystemParamRequest();
        request.setKey(SystemParamType.min_stock_visibility);
        request.setValue("20");

        when(systemParamsRepository.existsById(SystemParamType.min_stock_visibility)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> systemParamService.create(request));
        verify(systemParamsRepository, times(1)).existsById(SystemParamType.min_stock_visibility);
        verify(systemParamsRepository, never()).save(any(SystemParamEntity.class));
    }

    @Test
    @DisplayName("Should update system param successfully")
    void testUpdate_Success() {
        // Arrange
        UpdateSystemParamRequest request = new UpdateSystemParamRequest();
        request.setValue("25");

        when(systemParamsRepository.findById(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.of(systemParamEntity));
        when(systemParamsRepository.save(any(SystemParamEntity.class))).thenReturn(systemParamEntity);

        // Act
        SystemParamResponse result = systemParamService.update(SystemParamType.min_stock_visibility, request);

        // Assert
        assertNotNull(result);
        verify(systemParamsRepository, times(1)).findById(SystemParamType.min_stock_visibility);
        verify(systemParamsRepository, times(1)).save(any(SystemParamEntity.class));
    }

    @Test
    @DisplayName("Should find one system param successfully")
    void testFindOne_Success() {
        // Arrange
        when(systemParamsRepository.findById(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.of(systemParamEntity));

        // Act
        SystemParamResponse result = systemParamService.findOne(SystemParamType.min_stock_visibility);

        // Assert
        assertNotNull(result);
        assertEquals(SystemParamType.min_stock_visibility, result.getKey());
        verify(systemParamsRepository, times(1)).findById(SystemParamType.min_stock_visibility);
    }

    @Test
    @DisplayName("Should get as string with default value")
    void testGetAsString_WithDefault() {
        // Arrange
        when(systemParamsRepository.findByKey(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.of(systemParamEntity));

        // Act
        String result = systemParamService.getAsString(SystemParamType.min_stock_visibility, "default");

        // Assert
        assertEquals("15", result);
    }

    @Test
    @DisplayName("Should return default value when param not found")
    void testGetAsString_NotFound() {
        // Arrange
        when(systemParamsRepository.findByKey(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.empty());

        // Act
        String result = systemParamService.getAsString(SystemParamType.min_stock_visibility, "default");

        // Assert
        assertEquals("default", result);
    }

    @Test
    @DisplayName("Should get as int successfully")
    void testGetAsInt_Success() {
        // Arrange
        when(systemParamsRepository.findByKey(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.of(systemParamEntity));

        // Act
        int result = systemParamService.getAsInt(SystemParamType.min_stock_visibility, 10);

        // Assert
        assertEquals(15, result);
    }

    @Test
    @DisplayName("Should get as double successfully")
    void testGetAsDouble_Success() {
        // Arrange
        SystemParamEntity doubleEntity = new SystemParamEntity(SystemParamType.prob_payment_reject, "0.15");
        when(systemParamsRepository.findByKey(SystemParamType.prob_payment_reject))
                .thenReturn(Optional.of(doubleEntity));

        // Act
        double result = systemParamService.getAsDouble(SystemParamType.prob_payment_reject, 0.1);

        // Assert
        assertEquals(0.15, result, 0.001);
    }

    @Test
    @DisplayName("Should get as boolean successfully")
    void testGetAsBoolean_Success() {
        // Arrange
        SystemParamEntity boolEntity = new SystemParamEntity(SystemParamType.min_stock_visibility, "true");
        when(systemParamsRepository.findByKey(SystemParamType.min_stock_visibility))
                .thenReturn(Optional.of(boolEntity));

        // Act
        boolean result = systemParamService.getAsBoolean(SystemParamType.min_stock_visibility, false);

        // Assert
        assertTrue(result);
    }
}

