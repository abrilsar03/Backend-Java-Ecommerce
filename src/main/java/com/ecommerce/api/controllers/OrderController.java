package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.orders.CheckoutRequest;
import com.ecommerce.api.dto.orders.OrderResponse;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.*;
import com.ecommerce.api.services.OrderService;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final UserRepository users;
    private final CartRepository carts;
    private final OrderService orderService;

    public OrderController(UserRepository users, CartRepository carts, OrderService orderService) {
        this.users = users;
        this.carts = carts;
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public Object checkout(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody CheckoutRequest body) {

        var order = orderService.placeOrder(auth.getId(), body.getShippingAddress(),
                body.getCardToken());

        return Map.of("orderId", order.getId(), "status", order.getStatus().name());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable UUID id) {
        return orderService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PaginatedResponse<OrderResponse> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> ordersPage = orderService.findAll(page, size);
        return PaginatedResponse.from(ordersPage, order -> order);
    }
}
