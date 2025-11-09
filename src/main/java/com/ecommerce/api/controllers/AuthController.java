package com.ecommerce.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.api.dto.auth.AuthResponse;
import com.ecommerce.api.dto.auth.LoginUserRequest;
import com.ecommerce.api.dto.auth.RegisterUserRequest;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/current-user/register")
    public AuthResponse registerCurrentUser(@Valid @RequestBody RegisterUserRequest request) {
        return authService.register(request, RoleCodeType.CLIENT);

    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterUserRequest request) {

        return authService.register(request, RoleCodeType.ADMIN);

    }



    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.login(request)).getBody();
    }

}
