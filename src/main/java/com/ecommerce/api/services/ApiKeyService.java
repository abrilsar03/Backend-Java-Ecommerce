package com.ecommerce.api.services;

import com.ecommerce.api.dto.apiKeys.ApiKeyResponse;
import com.ecommerce.api.dto.apiKeys.CreateApiKeyRequest;
import com.ecommerce.api.entities.ApiKeyEntity;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.ApiKeyRepository;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiKeyService {

    private static final int RAW_KEY_BYTES = 32;

    private final ApiKeyRepository apiKeyRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Transactional
    public ApiKeyResponse create(CreateApiKeyRequest request) {

        Optional<ApiKeyEntity> existingApiKey =
                apiKeyRepository.findByNameAndActiveTrue(request.getName());


        if (existingApiKey.isPresent()) {
            return parseResponse(existingApiKey.get());
        }

        String key = generateKey();
        ApiKeyEntity entity = new ApiKeyEntity(request.getName(), key);
        ApiKeyEntity saved = apiKeyRepository.save(entity);

        return parseResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ApiKeyResponse> listAll() {
        return apiKeyRepository.findAll().stream().map(this::parseResponse).toList();
    }

    @Transactional
    public ApiKeyResponse deactivate(UUID id) {
        ApiKeyEntity entity =
                apiKeyRepository.findById(id).orElseThrow(ExceptionFactory::apiKeyNotFound);
        entity.setActive(false);
        ApiKeyEntity saved = apiKeyRepository.save(entity);
        return parseResponse(saved);
    }

    private ApiKeyResponse parseResponse(ApiKeyEntity entity) {
        return new ApiKeyResponse(entity.getId(), entity.getName(), entity.getKey(),
                entity.getActive(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private String generateKey() {
        byte[] buffer = new byte[RAW_KEY_BYTES];
        secureRandom.nextBytes(buffer);
        return "api_" + Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }
}

