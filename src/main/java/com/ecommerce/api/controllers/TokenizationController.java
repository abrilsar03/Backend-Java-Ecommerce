// controllers/TokenizationController.java
package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.tokenization.*;
import com.ecommerce.api.entities.ApiKeyEntity;
import com.ecommerce.api.services.TokenizationService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokenization")
public class TokenizationController {

    private final TokenizationService service;

    public TokenizationController(TokenizationService service) {
        this.service = service;
    }

    @PostMapping("/cards")
    public TokenizeCardResponse tokenize(@AuthenticationPrincipal ApiKeyEntity apiKey,
            @Valid @RequestBody TokenizeCardRequest body) {
        return service.tokenize(apiKey, body);
    }
}
