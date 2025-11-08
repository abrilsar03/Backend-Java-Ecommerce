package com.ecommerce.api.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ecommerce.api.exceptions.ApiException;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.utils.AuthUserJwtUtils;
import io.micrometer.common.lang.NonNull;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUserJwtUtils authUserJwtUtils;

    private final UserRepository userRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthFilter(AuthUserJwtUtils authUserJwtUtils, UserRepository userRepository) {
        this.authUserJwtUtils = authUserJwtUtils;
        this.userRepository = userRepository;
    }

    private static final List<String> EXCLUDED_PATHS = List.of("/auth/login", "/health");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                AuthUser authUser = authUserJwtUtils.validateAndExtractToken(token);

                if (authUser == null) {
                    throw new ApiException("401-user");
                }

                if (!userRepository.existsById(authUser.getId())) {
                    throw new ApiException("401-user");
                }

                request.setAttribute("authUser", authUser);

                var authenticationToken = new UsernamePasswordAuthenticationToken(authUser, null,
                        authUser.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (ApiException ex) {
                SecurityContextHolder.clearContext();
                throw new ApiException("401-user");
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
