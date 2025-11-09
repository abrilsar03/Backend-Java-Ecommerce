package com.ecommerce.api.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "search_logs",
        indexes = {@Index(name = "idx_search_logs_user", columnList = "user_id"),
                @Index(name = "idx_search_logs_created_at", columnList = "created_at")})

public class SearchLogEntity extends BasicEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "endpoint", nullable = false, length = 120)
    private String endpoint;

    @Column(name = "query", length = 2000)
    private String query;

    @Column(name = "ip", length = 64)
    private String ip;

    @Column(name = "user_agent", length = 512)
    private String userAgent;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
