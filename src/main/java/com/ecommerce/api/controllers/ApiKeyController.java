package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.apiKeys.ApiKeyResponse;
import com.ecommerce.api.dto.apiKeys.CreateApiKeyRequest;
import com.ecommerce.api.services.ApiKeyService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-keys")
@PreAuthorize("hasRole('ADMIN')")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/create")
    public ApiKeyResponse create(@Valid @RequestBody CreateApiKeyRequest request) {
        return apiKeyService.create(request);
    }

    @GetMapping
    public List<ApiKeyResponse> list() {
        return apiKeyService.listAll();
    }

    @PatchMapping("/{id}/deactivate")
    public ApiKeyResponse deactivate(@PathVariable UUID id) {
        return apiKeyService.deactivate(id);
    }
}

