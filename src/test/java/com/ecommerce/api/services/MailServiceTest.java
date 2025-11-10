package com.ecommerce.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MailService Tests")
class MailServiceTest {

    @Mock
    private JavaMailSender sender;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("Should send payment success email")
    void testPaymentSuccess_Success() {
        // Arrange
        String to = "user@example.com";
        String orderId = "12345";

        doNothing().when(sender).send(any(SimpleMailMessage.class));

        // Act
        mailService.paymentSuccess(to, orderId);

        // Assert
        verify(sender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send payment failed email")
    void testPaymentFailed_Success() {
        // Arrange
        String to = "user@example.com";
        String orderId = "12345";
        int attempts = 3;

        doNothing().when(sender).send(any(SimpleMailMessage.class));

        // Act
        mailService.paymentFailed(to, orderId, attempts);

        // Assert
        verify(sender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should handle email sending exception gracefully")
    void testSendEmail_ExceptionHandling() {
        // Arrange
        String to = "user@example.com";
        String orderId = "12345";

        doThrow(new RuntimeException("Email error")).when(sender).send(any(SimpleMailMessage.class));

        // Act & Assert - Should not throw exception
        try {
            mailService.paymentSuccess(to, orderId);
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
        verify(sender, times(1)).send(any(SimpleMailMessage.class));
    }
}

