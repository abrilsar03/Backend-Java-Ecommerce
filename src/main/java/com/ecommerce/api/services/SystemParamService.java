// services/SystemParamService.java
package com.ecommerce.api.services;

import com.ecommerce.api.dto.systemParams.SystemParamResponse;
import com.ecommerce.api.dto.systemParams.UpsertSystemParamRequest;
import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.dto.systemParams.UpdateSystemParamRequest;
import com.ecommerce.api.entities.SystemParamEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.SystemParamRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class SystemParamService {

    private final SystemParamRepository repo;

    public SystemParamService(SystemParamRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public SystemParamResponse create(UpsertSystemParamRequest request) {
        if (repo.existsById(request.getKey())) {
            throw ExceptionFactory.systemParamAlreadyExists();
        }

        var systemParam = new SystemParamEntity(request.getKey(), request.getValue());


        return parseResponse(repo.save(systemParam));
    }

    @Transactional
    public SystemParamResponse update(SystemParamType key, UpdateSystemParamRequest request) {
        var systemParam =
                repo.findById(key).orElseThrow(() -> ExceptionFactory.systemParamNotFound());

        systemParam.setValue(request.getValue());

        var updated = repo.save(systemParam);

        return parseResponse(updated);
    }

    public SystemParamResponse findOne(SystemParamType key) {
        var systemParam =
                repo.findById(key).orElseThrow(() -> ExceptionFactory.systemParamNotFound());
        return parseResponse(systemParam);
    }

    public PaginatedResponse<SystemParamResponse> paginate(String q, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "key"));
        var p = repo.search(q, pageable);
        return PaginatedResponse.from(p, this::parseResponse);
    }

    public String getAsString(SystemParamType key, String defaultValue) {
        return repo.findById(key).map(SystemParamEntity::getValue).orElse(defaultValue);
    }

    public int getAsInt(SystemParamType key, int defaultValue) {
        try {
            return Integer.parseInt(getAsString(key, String.valueOf(defaultValue)));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public double getAsDouble(SystemParamType key, double defaultValue) {
        try {
            return Double.parseDouble(getAsString(key, String.valueOf(defaultValue)));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public boolean getAsBoolean(SystemParamType key, boolean defaultValue) {
        var s = getAsString(key, String.valueOf(defaultValue));
        return s.equalsIgnoreCase("true") || s.equals("1") || s.equalsIgnoreCase("yes");
    }

    private SystemParamResponse parseResponse(SystemParamEntity systemParam) {
        var dto = new SystemParamResponse();
        dto.setKey(systemParam.getKey());
        dto.setValue(systemParam.getValue());
        dto.setUpdatedAt(systemParam.getUpdatedAt());
        return dto;
    }
}
