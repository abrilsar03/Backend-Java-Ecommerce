package com.ecommerce.api.services;

import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductQuery;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.entities.ProductEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.ProductRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SystemParamService systemParamsService;

    @InjectMocks
    private ProductService productService;

    private ProductEntity productEntity;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setTitle("Test Product");
        productEntity.setSku("SKU-001");
        productEntity.setDescription("Test Description");
        productEntity.setPriceCents(10000);
        productEntity.setPhotoUrl("http://example.com/photo.jpg");
        productEntity.setTax(BigDecimal.valueOf(10.0));
        productEntity.setActive(true);
        productEntity.setStock(50);
    }

    @Test
    @DisplayName("Should search public products successfully")
    void testSearchPublic_Success() {
        // Arrange
        ProductQuery query = new ProductQuery();
        query.setPage(1);
        query.setSize(10);
        query.setName("Test");

        Page<ProductEntity> page = new PageImpl<>(List.of(productEntity), PageRequest.of(0, 10), 1);
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        lenient().when(systemParamsService.getAsInt(any(SystemParamType.class), anyInt())).thenReturn(15);

        // Act
        PaginatedResponse<ProductResponse> result = productService.searchPublic(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should search admin products successfully")
    void testSearchAdmin_Success() {
        // Arrange
        ProductQuery query = new ProductQuery();
        query.setPage(1);
        query.setSize(10);

        Page<ProductEntity> page = new PageImpl<>(List.of(productEntity), PageRequest.of(0, 10), 1);
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        PaginatedResponse<ProductResponse> result = productService.searchAdmin(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should find one public product successfully")
    void testFindOnePublic_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(systemParamsService.getAsInt(any(SystemParamType.class), anyInt())).thenReturn(15);

        // Act
        ProductResponse result = productService.findOnePublic(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getTitle());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Should throw exception when product not found for public")
    void testFindOnePublic_ProductNotFound() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.findOnePublic(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Should throw exception when product is not visible")
    void testFindOnePublic_ProductNotVisible() {
        // Arrange
        productEntity.setActive(false);
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(systemParamsService.getAsInt(any(SystemParamType.class), anyInt())).thenReturn(15);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.findOnePublic(productId));
    }

    @Test
    @DisplayName("Should find one admin product successfully")
    void testFindOneAdmin_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        // Act
        ProductResponse result = productService.findOneAdmin(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreate_Success() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setTitle("New Product");
        request.setSku("SKU-002");
        request.setDescription("New Description");
        request.setPrice(new BigDecimal("200.00"));
        request.setPhotoUrl("http://example.com/new.jpg");
        request.setTax(BigDecimal.valueOf(15.0));
        request.setStock(100);

        when(productRepository.findBySku("SKU-002")).thenReturn(Optional.empty());
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        ProductResponse result = productService.create(request);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findBySku("SKU-002");
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when SKU already exists")
    void testCreate_SkuAlreadyExists() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("SKU-001");

        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(productEntity));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.create(request));
        verify(productRepository, times(1)).findBySku("SKU-001");
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdate_Success() {
        // Arrange
        UpdateProductRequest request = new UpdateProductRequest();
        request.setTitle("Updated Title");
        request.setPrice(new BigDecimal("150.00"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        ProductResponse result = productService.update(productId, request);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void testUpdate_ProductNotFound() {
        // Arrange
        UpdateProductRequest request = new UpdateProductRequest();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.update(productId, request));
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(ProductEntity.class));
    }
}

