package com.ecommerce.api.services;

import com.ecommerce.api.dto.cart.*;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.CartStatusType;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Tests")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SystemParamService systemParamsService;

    @InjectMocks
    private CartService cartService;

    private UUID userId;
    private UserEntity userEntity;
    private CartEntity cartEntity;
    private ProductEntity productEntity;
    private CartItemEntity cartItemEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@example.com");

        productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID());
        productEntity.setTitle("Test Product");
        productEntity.setSku("SKU-001");
        productEntity.setPriceCents(10000);
        productEntity.setActive(true);
        productEntity.setStock(50);

        cartEntity = new CartEntity();
        cartEntity.setId(UUID.randomUUID());
        cartEntity.setUser(userEntity);
        cartEntity.setStatus(CartStatusType.ACTIVE);
        cartEntity.setItems(new HashSet<>());

        cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(UUID.randomUUID());
        cartItemEntity.setCart(cartEntity);
        cartItemEntity.setProduct(productEntity);
        cartItemEntity.setQuantity(2);
    }

    @Test
    @DisplayName("Should find active cart successfully")
    void testFindActive_Success() {
        // Arrange
        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));

        // Act
        CartResponse result = cartService.findActive(userId);

        // Assert
        assertNotNull(result);
        assertEquals(cartEntity.getId(), result.getCartId());
        verify(cartRepository, times(1)).findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE);
    }

    @Test
    @DisplayName("Should create active cart when not found")
    void testFindActive_CreateNewCart() {
        // Arrange
        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        // Act
        CartResponse result = cartService.findActive(userId);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    @DisplayName("Should add items to cart successfully")
    void testAddItems_Success() {
        // Arrange
        AddItemsRequest request = new AddItemsRequest();
        AddItemsRequest.Item itemRequest = new AddItemsRequest.Item();
        itemRequest.setProductId(productEntity.getId());
        itemRequest.setQuantity(3);
        request.setItems(List.of(itemRequest));

        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        // Act
        CartResponse result = cartService.addItems(userId, request);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productEntity.getId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when items are null or empty")
    void testAddItems_EmptyItems() {
        // Arrange
        AddItemsRequest request = new AddItemsRequest();
        request.setItems(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cartService.addItems(userId, request));
    }

    @Test
    @DisplayName("Should set quantities successfully")
    void testSetQuantities_Success() {
        // Arrange
        UpdateItemRequest request = new UpdateItemRequest();
        request.setProductId(productEntity.getId());
        request.setQuantity(5);

        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(systemParamsService.getAsInt(any(SystemParamType.class), anyInt())).thenReturn(15);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        // Act
        CartResponse result = cartService.setQuantities(userId, request);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productEntity.getId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    @DisplayName("Should remove items successfully")
    void testRemoveItems_Success() {
        // Arrange
        RemoveItemsRequest request = new RemoveItemsRequest();
        request.setProductIds(List.of(productEntity.getId()));

        cartEntity.getItems().add(cartItemEntity);

        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        // Act
        CartResponse result = cartService.removeItems(userId, request);

        // Assert
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    @DisplayName("Should calculate totals successfully")
    void testCalculateTotals_Success() {
        // Arrange
        cartEntity.getItems().add(cartItemEntity);

        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));

        // Act
        CartTotalsResponse result = cartService.calculateTotals(userId);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getSubtotal());
        assertNotNull(result.getTax());
        assertNotNull(result.getTotal());
        verify(cartRepository, times(1)).findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE);
    }

    @Test
    @DisplayName("Should return zero totals for empty cart")
    void testCalculateTotals_EmptyCart() {
        // Arrange
        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));

        // Act
        CartTotalsResponse result = cartService.calculateTotals(userId);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO.setScale(2), result.getSubtotal());
        assertEquals(BigDecimal.ZERO.setScale(2), result.getTax());
        assertEquals(BigDecimal.ZERO.setScale(2), result.getTotal());
    }
}

