package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.ProfileResponse;
import com.ecommerce.api.dto.updateUserRequest;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.services.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping("/current-user/profile")
    public ProfileResponse getCurrentUserProfile(@AuthenticationPrincipal AuthUser auth) {
        return userService.findUser(auth.getId());
    }

    @PatchMapping
    @RequestMapping("/current-user/profile")
    public ProfileResponse updateCurrentUserProfile(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody updateUserRequest body) {
        return userService.updateUser(auth.getId(), body);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/{id}")
    public ProfileResponse getUser(@PathVariable("id") UUID userId) {
        return userService.findUser(userId);
    }


}
