package com.ecommerce.api.services;

import com.ecommerce.api.dto.cart.*;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.CartStatusType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
        try {
            CartEntity cart =
                    cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                            .orElseGet(() -> createActiveCart(userId));

            return parseResponse(cart);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

        for (var item : request.getItems()) {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> ExceptionFactory.productNotFound());

            var existing = byProduct.get(product.getId());

            int desired = (existing == null ? 0 : existing.getQuantity()) + item.getQuantity();

            desired = clampUiLimits(desired);

            if (existing == null) {
                var cartItem = createCartItem(cart, product, desired);
                cart.getItems().add(cartItem);
                byProduct.put(product.getId(), cartItem);
            } else {
                existing.setQuantity(desired);
            }
        }

        var update = cartRepository.save(cart);

        return parseResponse(update);
    }

    @Transactional
    public CartResponse setQuantities(UUID userId, UpdateItemRequest request) {

        var cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElseGet(() -> createActiveCart(userId));

        Map<UUID, CartItemEntity> byProduct = cart.getItems().stream()
                .collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> ExceptionFactory.productNotFound());

        int target = clampUiLimits(request.getQuantity());

        assertProductActive(product);
        assertStockForTargetQuantity(product, target);

        var existing = byProduct.get(product.getId());

        if (existing == null) {
            var item = createCartItem(cart, product, target);
            cart.getItems().add(item);
            byProduct.put(product.getId(), item);
        } else {
            existing.setQuantity(target);
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

    private CartResponse parseResponse(CartEntity cart) {
        var cartResponse = new CartResponse();

        cartResponse.setCartId(cart.getId());

        cartResponse.setUserId(cart.getUser().getId());

        cartResponse.setStatus(cart.getStatus());

        var items = cart.getItems().stream().map(cartItem -> {
            var response = new CartItemResponse();

            var product = cartItem.getProduct();

            response.setProductId(product.getId());

            response.setTitle(product.getTitle());

            response.setSku(product.getSku());

            response.setQuantity(cartItem.getQuantity());

            response.setPriceCents(product.getPriceCents());

            return response;
        }).toList();

        cartResponse.setItems(items);

        return cartResponse;
    }

    private void assertProductActive(ProductEntity p) {
        if (Boolean.FALSE.equals(p.getActive())) {
            throw ExceptionFactory.productNotFound();
        }
    }

    private void assertStockForTargetQuantity(ProductEntity product, int targetQty) {
        Integer stock = product.getStock();

        if (stock != null && targetQty > stock) {
            throw ExceptionFactory.insufficientStock();
        }
    }

    private int clampUiLimits(int quantity) {
        if (quantity < 1)
            return 1;

        if (quantity > 100)
            return 100;

        return quantity;
    }

    private CartItemEntity createCartItem(CartEntity cart, ProductEntity product, int desired) {
        var cartItem = new CartItemEntity();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(desired);
        return cartItem;
    }

}
