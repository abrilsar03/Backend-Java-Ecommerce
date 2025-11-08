package com.ecommerce.api.utils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import javax.crypto.SecretKey;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.ecommerce.api.model.JwtPayload;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import com.ecommerce.api.exceptions.ApiException;

public abstract class JwtUtil<T extends JwtPayload> {
    @Value("${security.jwt.secret}")
    protected String jwtSecret;

    @Value("${security.jwt.expires-in}")
    protected long jwtExpiration;

    protected SecretKey jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(T payload) {
        if (payload == null) {
            throw new ApiException("Payload cannot be null");
        }

        String subject = getSubject(payload);

        Map<String, Object> safeClaims = new HashMap<>();

        safeClaims.put("userId", payload.getId());

        long expirationMs = calculateExpiration();

        return Jwts.builder().subject(subject).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .id(UUID.randomUUID().toString()).claims(safeClaims).signWith(jwtKey).compact();
    }

    public T validateAndExtractToken(String token) {
        try {
            // Jwt<?, ?> jwt = Jwts.parser().verifyWith(jwtKey).build().parse(token);

            // Claims claims = (Claims) jwt.getPayload();

            // Map<String, Object> claimsMap = new HashMap<>();
            // claims.forEach(claimsMap::put);

            // Object objectMapper;
            // T payload = objectMapper.convertValue(claimsMap, getPayloadType());

            // if (payload.getId() == null) {
            // throw new ApiException("Token payload missing ID");
            // }

            // return payload;
            return null;

        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new ApiException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new ApiException("Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new ApiException("Unsupported token");
        } catch (Exception e) {
            throw new ApiException("Error processing token: " + e.getMessage());
        }
    }

    private long calculateExpiration() {
        return jwtExpiration * 1000L;
    }

    private String getSubject(T payload) {
        return payload.getId() != null ? payload.getId().toString() : UUID.randomUUID().toString();
    }
}
