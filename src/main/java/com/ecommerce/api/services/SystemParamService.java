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
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class SystemParamService {

    private final SystemParamRepository systemParamsRepository;

    public SystemParamService(SystemParamRepository systemParamsRepository) {
        this.systemParamsRepository = systemParamsRepository;
    }

    @Transactional
    public SystemParamResponse create(UpsertSystemParamRequest request) {
        if (systemParamsRepository.existsById(request.getKey())) {
            throw ExceptionFactory.systemParamAlreadyExists();
        }

        var systemParam = new SystemParamEntity(request.getKey(), request.getValue());


        return parseResponse(systemParamsRepository.save(systemParam));
    }

    @Transactional
    public SystemParamResponse update(SystemParamType key, UpdateSystemParamRequest request) {
        var systemParam = systemParamsRepository.findById(key)
                .orElseThrow(() -> ExceptionFactory.systemParamNotFound());

        systemParam.setValue(request.getValue());

        var updated = systemParamsRepository.save(systemParam);

        return parseResponse(updated);
    }

    public SystemParamResponse findOne(SystemParamType key) {
        var systemParam = systemParamsRepository.findById(key)
                .orElseThrow(() -> ExceptionFactory.systemParamNotFound());
        return parseResponse(systemParam);
    }

    public PaginatedResponse<SystemParamResponse> paginate(String q, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "key"));
        var p = systemParamsRepository.search(q, pageable);
        return PaginatedResponse.from(p, this::parseResponse);
    }

    public String getAsString(SystemParamType key, String defaultValue) {
        return systemParamsRepository.findByKey(key).map(SystemParamEntity::getValue)
                .filter(value -> value != null && !value.trim().isEmpty()).orElse(defaultValue);
    }

    public int getAsInt(SystemParamType key, int defaultValue) {
        return systemParamsRepository.findByKey(key).map(SystemParamEntity::getValue).map(value -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }).orElse(defaultValue);
    }

    public double getAsDouble(SystemParamType key, double defaultValue) {
        return systemParamsRepository.findByKey(key).map(SystemParamEntity::getValue).map(value -> {
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }).orElse(defaultValue);
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
