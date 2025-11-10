// security/ApiKeyAuthFilter.java
package com.ecommerce.api.security;

import com.ecommerce.api.entities.ApiKeyEntity;
import com.ecommerce.api.enums.AuthorityType;
import com.ecommerce.api.repositories.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class ApiKeyAuth extends OncePerRequestFilter {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuth(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // Solo procesar si no hay autenticación previa
        if (SecurityContextHolder.getContext().getAuthentication() == null
                || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

            String apiKey = request.getHeader("X-API-KEY");

            if (StringUtils.hasText(apiKey)) {
                apiKeyRepository.findByKeyAndActiveTrue(apiKey).ifPresent(found -> {
                    Authentication auth = new ApiKeyAuthentication(found);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            }
        }

        chain.doFilter(request, response);
    }

    static class ApiKeyAuthentication extends AbstractAuthenticationToken {
        private final ApiKeyEntity principal;

        ApiKeyAuthentication(ApiKeyEntity principal) {
            super(List.of(new SimpleGrantedAuthority(AuthorityType.API_CLIENT.name())));
            this.principal = principal;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return "";
        }

        @Override
        public Object getPrincipal() {
            return principal;
        }
    }
}
