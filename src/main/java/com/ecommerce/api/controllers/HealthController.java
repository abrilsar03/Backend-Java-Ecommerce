package com.ecommerce.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health-check")
public class HealthController {

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "ok");
        body.put("service", "ecommerce-api");
        body.put("timestamp", OffsetDateTime.now().toString());
        return ResponseEntity.ok(body);
    }
}
