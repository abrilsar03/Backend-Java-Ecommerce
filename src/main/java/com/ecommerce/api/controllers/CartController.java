// src/main/java/com/ecommerce/api/controllers/CartController.java
package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.cart.*;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.services.CartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated() and hasRole('CLIENT')")
public class CartController {

    private final CartService carts;

    public CartController(CartService carts) {
        this.carts = carts;
    }

    @GetMapping()
    public CartResponse findActive(@AuthenticationPrincipal AuthUser auth) {
        return carts.findActive(auth.getId());
    }

    @PatchMapping("/add-items")
    public CartResponse addItems(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody AddItemsRequest body) {
        return carts.addItems(auth.getId(), body);
    }

    @PatchMapping("/edit-items")
    public CartResponse setQuantities(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody UpdateItemRequest body) {
        return carts.setQuantities(auth.getId(), body);
    }

    @PatchMapping("/remove-items")
    public CartResponse removeItems(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody RemoveItemsRequest body) {
        return carts.removeItems(auth.getId(), body);
    }
}
