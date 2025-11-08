package com.ecommerce.api.filters;

// JwtAuthFilter.java
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ecommerce.api.exceptions.ApiException;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.utils.AuthUserJwtUtils;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUserJwtUtils jwt;

    public JwtAuthFilter(AuthUserJwtUtils jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                AuthUser payload = jwt.validateAndExtractToken(token);

                if (payload == null) {
                    throw new ApiException("401-user");
                }

                var userAuth = new UsernamePasswordAuthenticationToken(payload, null,
                        payload.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(userAuth);

            } catch (ApiException ex) {
                SecurityContextHolder.clearContext();
                throw new ApiException("401-user");
            }
        }
        chain.doFilter(req, res);
    }
}
