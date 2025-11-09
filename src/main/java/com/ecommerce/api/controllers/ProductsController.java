
package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.services.ProductService;
import com.ecommerce.api.services.SearchLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService products;
    private final SearchLogService searchLog;

    public ProductsController(ProductService products, SearchLogService searchLog) {
        this.products = products;
        this.searchLog = searchLog;
    }

    @GetMapping("/current-user/paginate")
    public PaginatedResponse<ProductResponse> list(@RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AuthUser auth, HttpServletRequest req) {

        UUID userId = auth != null ? auth.getId() : null;

        String query = "q=" + (q == null ? "" : q) + "&page=" + page + "&size=" + size;

        searchLog.logAsync(userId, "/products", query, req.getRemoteAddr(),
                req.getHeader("User-Agent"));

        return products.searchPublic(q, page, size);
    }

    @GetMapping("/current-user/{id}")
    public ProductResponse findOnePublic(@PathVariable UUID id) {
        return products.findOnePublic(id);
    }


    @PreAuthorize("hasRole('ADMIN') || hasAuthority('PRODUCT:READ')")
    @GetMapping("/paginate")
    public PaginatedResponse<ProductResponse> list(@RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return products.searchAdmin(q, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('PRODUCT:READ')")
    public ProductResponse findOneAdmin(@PathVariable UUID id) {
        return products.findOneAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN') || hasAuthority('PRODUCT:CREATE')")
    @PostMapping
    public ProductResponse create(@Valid @RequestBody CreateProductRequest body) {
        return products.create(body);
    }


    @PreAuthorize("hasRole('ADMIN') || hasAuthority('PRODUCT:UPDATE')")
    @PatchMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest body) {
        return products.update(id, body);
    }
}
