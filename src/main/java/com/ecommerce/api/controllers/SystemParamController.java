// controllers/SystemParamController.java
package com.ecommerce.api.controllers;

import com.ecommerce.api.dto.systemParams.SystemParamResponse;
import com.ecommerce.api.dto.systemParams.UpsertSystemParamRequest;
import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.systemParams.UpdateSystemParamRequest;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.services.SystemParamService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-params")
@PreAuthorize("hasRole('ADMIN')")
public class SystemParamController {

    private final SystemParamService service;

    public SystemParamController(SystemParamService service) {
        this.service = service;
    }

    @GetMapping
    public PaginatedResponse<SystemParamResponse> list(@RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.paginate(query, page, size);
    }

    @GetMapping("/{key}")
    public com.ecommerce.api.dto.systemParams.SystemParamResponse getOne(
            @PathVariable("key") SystemParamType key) {
        return service.findOne(key);
    }

    @PostMapping
    public SystemParamResponse create(@Valid @RequestBody UpsertSystemParamRequest body) {
        return service.create(body);
    }

    @PatchMapping("/{key}")
    public SystemParamResponse update(@PathVariable("key") SystemParamType key,
            @Valid @RequestBody UpdateSystemParamRequest body) {
        return service.update(key, body);
    }
}
