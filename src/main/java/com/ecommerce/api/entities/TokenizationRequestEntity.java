package com.ecommerce.api.entities;

import com.ecommerce.api.enums.TokenizationStatusType;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name = "tokenization_requests",
        indexes = {@Index(name = "idxtokreq_fingerprint", columnList = "fingerprint"),
                @Index(name = "idxtokreq_status_created", columnList = "status, created_at")})

public class TokenizationRequestEntity extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id")
    private ApiKeyEntity apiKey;

    @Column
    private String fingerprint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenizationStatusType status;

    @Column
    private String reason;

    public TokenizationRequestEntity(ApiKeyEntity apiKey, String fingerprint,
            TokenizationStatusType status, @Nullable String reason) {
        super();
        this.apiKey = apiKey;
        this.fingerprint = fingerprint;
        this.status = status;
        this.reason = reason;
    }

    public ApiKeyEntity getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKeyEntity apiKey) {
        this.apiKey = apiKey;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public TokenizationStatusType getStatus() {
        return status;
    }

    public void setStatus(TokenizationStatusType status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

