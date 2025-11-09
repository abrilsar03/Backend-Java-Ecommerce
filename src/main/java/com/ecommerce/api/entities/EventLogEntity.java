package com.ecommerce.api.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.swing.event.DocumentEvent.EventType;
import com.ecommerce.api.enums.EntityType;
import com.ecommerce.api.enums.LogLevelType;

@Entity
@Table(name = "event_logs",
        indexes = {@Index(name = "idx_event_logs_request_id", columnList = "request_id"),
                @Index(name = "idx_event_logs_event_created",
                        columnList = "event_type, created_at"),
                @Index(name = "idx_event_logs_level_created", columnList = "level, created_at")})

public class EventLogEntity extends BasicEntity {

    @Column(name = "request_id", nullable = false)
    private UUID requestId;

    @Column(name = "event_type", nullable = false, length = 120)
    private EventType eventType;

    @Column(name = "entity_type", length = 120)
    private EntityType entityType;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(name = "level", nullable = false, length = 16)
    private LogLevelType level;

    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    public EventLogEntity(UUID requestId, EventType eventType, EntityType entityType, UUID entityId,
            LogLevelType level, String payload) {
        super();
        this.requestId = requestId;
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.level = level;
        this.payload = payload;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public LogLevelType getLevel() {
        return level;
    }

    public void setLevel(LogLevelType level) {
        this.level = level;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
