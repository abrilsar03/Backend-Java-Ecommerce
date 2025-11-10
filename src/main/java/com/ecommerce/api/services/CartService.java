package com.ecommerce.api.services;

import com.ecommerce.api.dto.cart.*;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.CartStatusType;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SystemParamService systemParamsService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository,
            UserRepository userRepository, SystemParamService systemParamsService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.systemParamsService = systemParamsService;
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

            response.setPriceFromCents(product.getPriceCents());

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
        int minStock = systemParamsService.getAsInt(SystemParamType.min_stock_visibility, 15);
        if (product.getStock() != null && targetQty > product.getStock()) {
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

    @Transactional(readOnly = true)
    public CartTotalsResponse calculateTotals(UUID userId) {
        CartEntity cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElseThrow(() -> ExceptionFactory.cartNotFound());

        if (cart.getItems().isEmpty()) {
            return new CartTotalsResponse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        int subtotalCents = 0;
        int taxCents = 0;

        for (var item : cart.getItems()) {
            var product = item.getProduct();

            if (product == null || Boolean.FALSE.equals(product.getActive())) {
                continue;
            }

            if (item.getQuantity() <= 0) {
                continue;
            }

            int itemTotalCents = product.getPriceCents() * item.getQuantity();
            subtotalCents += itemTotalCents;

            if (product.getTax() != null && product.getTax().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal taxDecimal =
                        product.getTax().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                BigDecimal itemTotal = new BigDecimal(itemTotalCents);
                BigDecimal itemTax = itemTotal.multiply(taxDecimal);
                taxCents += itemTax.setScale(0, RoundingMode.HALF_UP).intValue();
            }
        }

        int totalCents = subtotalCents + taxCents;

        BigDecimal subtotal = new BigDecimal(subtotalCents).divide(new BigDecimal("100"), 2,
                RoundingMode.HALF_UP);
        BigDecimal tax =
                new BigDecimal(taxCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal total =
                new BigDecimal(totalCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return new CartTotalsResponse(subtotal, tax, total);
    }

}
