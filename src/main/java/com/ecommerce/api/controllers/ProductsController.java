package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductQuery;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.services.ProductService;
import com.ecommerce.api.services.SearchLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsController {


    private final ProductService productService;
    private final SearchLogService searchLog;

    public ProductsController(ProductService productService, SearchLogService searchLog) {
        this.productService = productService;
        this.searchLog = searchLog;
    }

    @PreAuthorize("hasRole('CLIENT') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/current-user/paginate")
    public PaginatedResponse<ProductResponse> searchPublicProducts(ProductQuery query) {
        return productService.searchPublic(query);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/current-user/{id}")
    public ProductResponse findOnePublic(@PathVariable("id") UUID id) {
        return productService.findOnePublic(id);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/paginate")
    public PaginatedResponse<ProductResponse> listAdmin(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.searchAdmin(q, page, size);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/{id}")
    public ProductResponse findOneAdmin(@PathVariable("id") UUID id) {
        return productService.findOneAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:CREATE')")
    @PostMapping("/")
    public ProductResponse create(@Valid @RequestBody CreateProductRequest body) {
        return productService.create(body);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:UPDATE')")
    @PatchMapping("/{id}")
    public ProductResponse update(@PathVariable("id") UUID id,
            @Valid @RequestBody UpdateProductRequest body) {
        return productService.update(id, body);
    }
}
