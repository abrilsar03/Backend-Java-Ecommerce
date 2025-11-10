// security/ApiKeyAuthFilter.java
package com.ecommerce.api.security;

import com.ecommerce.api.entities.ApiKeyEntity;
import com.ecommerce.api.enums.AuthorityType;
import com.ecommerce.api.repositories.ApiKeyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class ApiKeyAuth implements Filter {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuth(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var http = (HttpServletRequest) request;

        String apiKey = http.getHeader("X-API-Key");

        if (StringUtils.hasText(apiKey)) {
            apiKeyRepository.findByKeyAndActiveTrue(apiKey).ifPresent(found -> {
                Authentication auth = new ApiKeyAuthentication(found);
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
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
