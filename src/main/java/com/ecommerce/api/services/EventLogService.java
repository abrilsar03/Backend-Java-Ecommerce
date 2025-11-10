package com.ecommerce.api.services;

import com.ecommerce.api.entities.EventLogEntity;
import com.ecommerce.api.enums.EntityType;
import com.ecommerce.api.enums.LogLevelType;
import com.ecommerce.api.enums.EventType;
import com.ecommerce.api.repositories.EventLogRepository;
import com.ecommerce.api.utils.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class EventLogService {

    private final EventLogRepository eventLogRepository;
    private final ObjectMapper mapper;

    public EventLogService(EventLogRepository eventLogRepository, ObjectMapper mapper) {
        this.eventLogRepository = eventLogRepository;
        this.mapper = mapper;
    }

    @Async("loggingExecutor")
    public void info(EventType eventType, EntityType entityType, UUID entityId,
            Map<String, ?> payload) {
        save(LogLevelType.INFO, eventType, entityType, entityId, payload);
    }

    @Async("loggingExecutor")
    public void warn(EventType eventType, EntityType entityType, UUID entityId,
            Map<String, ?> payload) {
        save(LogLevelType.INFO, eventType, entityType, entityId, payload);
    }

    @Async("loggingExecutor")
    public void error(EventType eventType, EntityType entityType, UUID entityId,
            Map<String, ?> payload) {
        save(LogLevelType.ERROR, eventType, entityType, entityId, payload);
    }

    private void save(LogLevelType level, EventType eventType, EntityType entityType, UUID entityId,
            Map<String, ?> payload) {
        try {
            UUID requestId = Optional.ofNullable(RequestContext.currentRequestId())
                    .orElse(UUID.randomUUID());

            String payloadJson = payload != null ? mapper.writeValueAsString(payload) : null;

            var event = new EventLogEntity(requestId, eventType, entityType, entityId, level,
                    payloadJson);

            eventLogRepository.save(event);

        } catch (Exception ignored) {
        }
    }
}
