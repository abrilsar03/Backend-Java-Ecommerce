package com.ecommerce.api.utils;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import com.ecommerce.api.model.JwtPayload;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.ecommerce.api.exceptions.ExceptionFactory;

public abstract class JwtUtil<T extends JwtPayload> {
    @Value("${jwt.secret}")
    protected String jwtSecret;

    @Value("${jwt.expires}")
    protected long jwtExpiration;

    protected SecretKey jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(T payload) {
        if (payload == null) {
            throw ExceptionFactory.invalidToken();
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
            Claims claims =
                    Jwts.parser().verifyWith(jwtKey).build().parseSignedClaims(token).getPayload();

            T payload = createPayloadInstance(claims);

            if (payload == null || payload.getId() == null) {
                throw ExceptionFactory.invalidToken();
            }

            return payload;

        } catch (Exception e) {
            throw ExceptionFactory.unauthorized("User not authorized");
        }
    }

    public long calculateExpiration() {
        return jwtExpiration * 1000L;
    }

    private String getSubject(T payload) {
        return payload.getId() != null ? payload.getId().toString() : UUID.randomUUID().toString();
    }

    protected abstract T createPayloadInstance(Claims claims);
}
