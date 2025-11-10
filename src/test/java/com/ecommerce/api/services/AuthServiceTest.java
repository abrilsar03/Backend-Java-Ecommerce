package com.ecommerce.api.services;

import com.ecommerce.api.dto.auth.AuthResponse;
import com.ecommerce.api.dto.auth.LoginUserRequest;
import com.ecommerce.api.dto.auth.RegisterUserRequest;
import com.ecommerce.api.entities.RoleEntity;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.RoleRepository;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private UserEntity userEntity;
    private RoleEntity roleEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@example.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        // Password is set via constructor with PasswordEncoder, we'll use reflection or mock it
        // For testing, we'll just set a mock password directly
        try {
            java.lang.reflect.Field passwordField = UserEntity.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(userEntity, "encodedPassword");
        } catch (Exception e) {
            // If reflection fails, we'll just proceed without setting password
        }
        userEntity.setActive(true);

        roleEntity = new RoleEntity();
        roleEntity.setRole(RoleCodeType.CLIENT);
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegister_Success() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password123");
        request.setFirstName("Jane");
        request.setLastName("Smith");

        when(userRepository.existsByEmailIgnoreCase("newuser@example.com")).thenReturn(false);
        when(roleRepository.findByRole(RoleCodeType.CLIENT)).thenReturn(roleEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtUtil.generateToken(any())).thenReturn("test-token");
        when(jwtUtil.calculateExpiration()).thenReturn(3600000L);

        // Act
        AuthResponse result = authService.register(request, RoleCodeType.CLIENT);

        // Assert
        assertNotNull(result);
        assertEquals("test-token", result.getAccessToken());
        verify(userRepository, times(1)).existsByEmailIgnoreCase("newuser@example.com");
        verify(roleRepository, times(1)).findByRole(RoleCodeType.CLIENT);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegister_EmailAlreadyExists() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmailIgnoreCase("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(request, RoleCodeType.CLIENT));
        verify(userRepository, times(1)).existsByEmailIgnoreCase("existing@example.com");
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLogin_Success() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("test-token");
        when(jwtUtil.calculateExpiration()).thenReturn(3600000L);

        // Act
        AuthResponse result = authService.login(request);

        // Assert
        assertNotNull(result);
        assertEquals("test-token", result.getAccessToken());
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLogin_UserNotFound() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("notfound@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("notfound@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(request));
        verify(userRepository, times(1)).findByEmailIgnoreCase("notfound@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void testLogin_IncorrectPassword() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(request));
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encodedPassword");
    }

    @Test
    @DisplayName("Should throw exception when user is inactive")
    void testLogin_InactiveUser() {
        // Arrange
        userEntity.setActive(false);
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}

