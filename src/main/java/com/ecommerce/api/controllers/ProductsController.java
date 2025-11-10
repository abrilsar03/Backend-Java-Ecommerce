package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductQuery;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.services.EventLogService;
import com.ecommerce.api.services.ProductService;
import com.ecommerce.api.services.SearchLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsController {


    private final ProductService productService;
    private final SearchLogService searchLog;
    private final EventLogService eventLog;

    public ProductsController(ProductService productService, SearchLogService searchLog,
            EventLogService eventLog) {
        this.productService = productService;
        this.searchLog = searchLog;
        this.eventLog = eventLog;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip.split(",")[0].trim() : "unknown";
    }

    private String buildQueryString(ProductQuery query) {
        StringBuilder sb = new StringBuilder();
        if (query.getName() != null)
            sb.append("name=").append(query.getName()).append("&");
        if (query.getSku() != null)
            sb.append("sku=").append(query.getSku()).append("&");
        if (query.getHasStock() != null)
            sb.append("hasStock=").append(query.getHasStock()).append("&");
        if (query.getMinPrice() != null)
            sb.append("minPrice=").append(query.getMinPrice()).append("&");
        if (query.getMaxPrice() != null)
            sb.append("maxPrice=").append(query.getMaxPrice()).append("&");
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    @GetMapping
    public PaginatedResponse<ProductResponse> listPublicProducts(ProductQuery query,
            HttpServletRequest request) {
        // Public endpoint - no authentication required
        return productService.searchPublic(query);
    }

    @PreAuthorize("hasRole('CLIENT') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/current-user/paginate")
    public PaginatedResponse<ProductResponse> searchPublicProducts(
            @AuthenticationPrincipal AuthUser auth, ProductQuery query,
            HttpServletRequest request) {
        // Log search asynchronously
        String queryString = buildQueryString(query);
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        searchLog.logAsync(auth.getId(), "/products/current-user/paginate", queryString, ip,
                userAgent);

        // Log event
        eventLog.info(com.ecommerce.api.enums.EventType.PRODUCT_SEARCH,
                com.ecommerce.api.enums.EntityType.PRODUCT, null, java.util.Map.of("userId",
                        auth.getId().toString(), "query", queryString != null ? queryString : ""));

        return productService.searchPublic(query);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/current-user/{id}")
    public ProductResponse findOnePublic(@PathVariable("id") UUID id) {
        return productService.findOnePublic(id);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/paginate")
    public PaginatedResponse<ProductResponse> searchAdminProducts(
            @AuthenticationPrincipal AuthUser auth, ProductQuery query,
            HttpServletRequest request) {
        // Log search asynchronously
        String queryString = buildQueryString(query);
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        searchLog.logAsync(auth != null ? auth.getId() : null, "/products/paginate", queryString,
                ip, userAgent);

        return productService.searchAdmin(query);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:READ')")
    @GetMapping("/{id}")
    public ProductResponse findOneAdmin(@PathVariable("id") UUID id) {
        return productService.findOneAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:CREATE')")
    @PostMapping("/")
    public ProductResponse create(@AuthenticationPrincipal AuthUser auth,
            @Valid @RequestBody CreateProductRequest body) {
        ProductResponse response = productService.create(body);

        // Log product creation
        eventLog.info(com.ecommerce.api.enums.EventType.PRODUCT_CREATED,
                com.ecommerce.api.enums.EntityType.PRODUCT, response.getId(),
                java.util.Map.of("sku", body.getSku(), "title", body.getTitle()));

        return response;
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('PRODUCT:UPDATE')")
    @PatchMapping("/{id}")
    public ProductResponse update(@AuthenticationPrincipal AuthUser auth,
            @PathVariable("id") UUID id, @Valid @RequestBody UpdateProductRequest body) {
        ProductResponse response = productService.update(id, body);

        // Log product update
        eventLog.info(com.ecommerce.api.enums.EventType.PRODUCT_UPDATED,
                com.ecommerce.api.enums.EntityType.PRODUCT, id, java.util.Map.of("active",
                        body.getActive() != null ? body.getActive().toString() : "unchanged"));

        return response;
    }
}
