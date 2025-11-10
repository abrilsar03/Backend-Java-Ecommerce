package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.checkout.CheckoutRequest;
import com.ecommerce.api.enums.CartStatusType;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.*;
import com.ecommerce.api.services.CheckoutService;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class CheckoutController {

    private final UserRepository users;
    private final CartRepository carts;
    private final CheckoutService checkout;

    public CheckoutController(UserRepository users, CartRepository carts,
            CheckoutService checkout) {
        this.users = users;
        this.carts = carts;
        this.checkout = checkout;
    }

    @PostMapping("/checkout")
    public Object checkout(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody CheckoutRequest body) {

        var order =
                checkout.placeOrder(auth.getId(), body.getShippingAddress(), body.getCardToken());

        return Map.of("orderId", order.getId(), "status", order.getStatus().name());
    }
}
