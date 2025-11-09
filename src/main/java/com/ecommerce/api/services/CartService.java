package com.ecommerce.api.services;

import com.ecommerce.api.dto.cart.*;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.CartStatusType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartResponse findActive(UUID userId) {
        CartEntity cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElse(null);

        if (cart == null) {
            return empty(userId);
        }

        return parseResponse(cart);
    }

    @Transactional
    public CartResponse addItems(UUID userId, AddItemsRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw ExceptionFactory.missingData("items are required");
        }

        var cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElseThrow(() -> ExceptionFactory.cartNotFound());

        Map<UUID, CartItemEntity> byProduct = cart.getItems().stream()
                .collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

        for (var in : request.getItems()) {
            var product = productRepository.findById(in.getProductId())
                    .orElseThrow(() -> ExceptionFactory.productNotFound());

            var existing = byProduct.get(product.getId());

            if (existing == null) {
                var it = new CartItemEntity();
                it.setCart(cart);
                it.setProduct(product);
                it.setQuantity(in.getQuantity());
                cart.getItems().add(it);
                byProduct.put(product.getId(), it);
            } else {
                int newQty = existing.getQuantity() + in.getQuantity();
                existing.setQuantity(Math.min(newQty, 100));
            }
        }

        var update = cartRepository.save(cart);

        return parseResponse(update);
    }

    @Transactional
    public CartResponse setQuantities(UUID userId, UpdateItemRequest request) {

        if (request.getProductId() == null) {
            throw ExceptionFactory.missingData("items are required");
        }

        var cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElse(null);

        if (cart == null) {
            cart = createActiveCart(userId);
        }

        Map<UUID, CartItemEntity> byProduct = cart.getItems().stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), item -> item));


        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> ExceptionFactory.productNotFound());

        var targetQty = Math.max(1, Math.min(request.getQuantity(), 100));

        var existingCart = byProduct.get(product.getId());

        if (existingCart == null) {
            var it = new CartItemEntity();
            it.setCart(cart);
            it.setProduct(product);
            it.setQuantity(targetQty);
            cart.getItems().add(it);
            byProduct.put(product.getId(), it);
        } else {
            existingCart.setQuantity(targetQty);
        }


        return parseResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItems(UUID userId, RemoveItemsRequest request) {
        if (request.getProductIds() == null || request.getProductIds().isEmpty()) {
            return findActive(userId);
        }

        var cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElseThrow(() -> ExceptionFactory.cartNotFound());


        Set<UUID> ids = new HashSet<>(request.getProductIds());

        cart.getItems().removeIf(item -> ids.contains(item.getProduct().getId()));

        return parseResponse(cartRepository.save(cart));
    }

    private CartEntity createActiveCart(UUID userId) {
        var user =
                userRepository.findById(userId).orElseThrow(() -> ExceptionFactory.userNotFound());

        var cart = new CartEntity();

        cart.setUser(user);

        cart.setStatus(CartStatusType.ACTIVE);

        return cartRepository.save(cart);
    }

    private CartResponse empty(UUID userId) {
        var cartResponse = new CartResponse();
        cartResponse.setCartId(null);
        cartResponse.setUserId(userId);
        cartResponse.setStatus(CartStatusType.ACTIVE);
        cartResponse.setItems(List.of());
        return cartResponse;
    }

    private CartResponse parseResponse(CartEntity cart) {
        var cartResponse = new CartResponse();

        cartResponse.setCartId(cart.getId());

        cartResponse.setUserId(cart.getUser().getId());

        cartResponse.setStatus(cart.getStatus());

        var items = cart.getItems().stream().map(ci -> {
            var response = new CartItemResponse();

            var product = ci.getProduct();

            response.setProductId(product.getId());

            response.setTitle(product.getTitle());

            response.setSku(product.getSku());

            response.setQuantity(ci.getQuantity());

            response.setPriceCents(product.getPriceCents());

            return response;
        }).toList();

        cartResponse.setItems(items);

        return cartResponse;
    }
}
