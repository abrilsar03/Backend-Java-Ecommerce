package com.ecommerce.api.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.api.dto.AuthResponse;
import com.ecommerce.api.dto.LoginUserRequest;
import com.ecommerce.api.dto.RegisterUserRequest;
import com.ecommerce.api.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterUserRequest req) {
        authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginUserRequest req) {
        return authService.login(req);
    }

    @PostMapping("/login")
    public AuthResponse logOut() {
        return authService.logOut(req);
    }


}
