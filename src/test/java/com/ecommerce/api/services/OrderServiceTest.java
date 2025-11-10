package com.ecommerce.api.services;

import com.ecommerce.api.dto.orders.OrderResponse;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.*;
import com.ecommerce.api.repositories.*;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orders;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PaymentRepository payments;

    @Mock
    private CardTokenRepository cardTokens;

    @Mock
    private SystemParamService params;

    @Mock
    private EventLogService eventLog;

    @Mock
    private MailService mailer;

    @InjectMocks
    private OrderService orderService;

    private UserEntity userEntity;
    private CartEntity cartEntity;
    private ProductEntity productEntity;
    private CartItemEntity cartItemEntity;
    private CardTokenEntity cardTokenEntity;
    private UUID userId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        orderId = UUID.randomUUID();

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

        cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(UUID.randomUUID());
        cartItemEntity.setProduct(productEntity);
        cartItemEntity.setQuantity(2);

        cartEntity = new CartEntity();
        cartEntity.setId(UUID.randomUUID());
        cartEntity.setUser(userEntity);
        cartEntity.setStatus(CartStatusType.ACTIVE);
        cartEntity.setItems(new HashSet<>(List.of(cartItemEntity)));

        cardTokenEntity = new CardTokenEntity("tok_123", "fingerprint123", "VISA", "1234", (short) 12, (short) 2025);
        cardTokenEntity.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should place order successfully")
    void testPlaceOrder_Success() {
        // Arrange
        String shippingAddress = "123 Main St";
        String cardToken = "tok_123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE))
                .thenReturn(Optional.of(cartEntity));
        when(cardTokens.findByToken(cardToken)).thenReturn(Optional.of(cardTokenEntity));
        when(params.getAsInt(SystemParamType.payment_retry_max, 3)).thenReturn(3);
        when(params.getAsDouble(SystemParamType.prob_payment_reject, 0.15)).thenReturn(0.0); // Always succeed
        when(orders.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity order = invocation.getArgument(0);
            order.setId(orderId);
            return order;
        });
        when(payments.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
        doNothing().when(eventLog).info(any(), any(), any(), any());
        doNothing().when(mailer).paymentSuccess(anyString(), anyString());

        // Act
        OrderEntity result = orderService.placeOrder(userId, shippingAddress, cardToken);

        // Assert
        assertNotNull(result);
        verify(orders, atLeastOnce()).save(any(OrderEntity.class));
        verify(payments, atLeastOnce()).save(any(PaymentEntity.class));
        verify(mailer, times(1)).paymentSuccess(anyString(), anyString());
    }

    @Test
    @DisplayName("Should find order by id successfully")
    void testFindById_Success() {
        // Arrange
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setUser(userEntity);
        orderEntity.setStatus(OrderStatusType.PAID);
        orderEntity.setShippingAddress("123 Main St");
        orderEntity.setSubtotalCents(20000);
        orderEntity.setTaxCents(0);
        orderEntity.setTotalCents(20000);
        orderEntity.setItems(new HashSet<>());

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(UUID.randomUUID());
        paymentEntity.setOrder(orderEntity);
        paymentEntity.setStatus(PaymentStatusType.SUCCESS);
        paymentEntity.setPaymentType(PaymentType.CARD);
        paymentEntity.setCardToken(cardTokenEntity);

        when(orders.findByIdWithItemsAndUser(orderId)).thenReturn(Optional.of(orderEntity));
        when(payments.findByOrderIdWithCardToken(orderId)).thenReturn(Optional.of(paymentEntity));

        // Act
        OrderResponse result = orderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orders, times(1)).findByIdWithItemsAndUser(orderId);
    }

    @Test
    @DisplayName("Should throw exception when order not found")
    void testFindById_NotFound() {
        // Arrange
        when(orders.findByIdWithItemsAndUser(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.findById(orderId));
        verify(orders, times(1)).findByIdWithItemsAndUser(orderId);
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void testFindAll_Success() {
        // Arrange
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setUser(userEntity);
        orderEntity.setStatus(OrderStatusType.PAID);
        orderEntity.setShippingAddress("123 Main St");
        orderEntity.setSubtotalCents(20000);
        orderEntity.setTaxCents(0);
        orderEntity.setTotalCents(20000);
        orderEntity.setItems(new HashSet<>());

        Page<OrderEntity> page = new PageImpl<>(List.of(orderEntity), PageRequest.of(0, 10), 1);

        when(orders.findAll(any(PageRequest.class))).thenReturn(page);
        when(payments.findByOrderIdWithCardToken(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Page<OrderResponse> result = orderService.findAll(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(orders, times(1)).findAll(any(PageRequest.class));
    }
}

