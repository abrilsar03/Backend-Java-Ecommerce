package com.ecommerce.api.filters;

import com.ecommerce.api.exceptions.ApiException;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.utils.AuthUserJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUserJwtUtils authUserJwtUtils;
    private final UserRepository userRepository;

    public JwtAuthFilter(AuthUserJwtUtils authUserJwtUtils, UserRepository userRepository) {
        this.authUserJwtUtils = authUserJwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var payload = authUserJwtUtils.validateAndExtractToken(token);

                UUID userId = payload.getId();

                if (userId == null) {
                    throw ExceptionFactory.invalidToken();
                }

                var user = userRepository.findByIdWithRolesAndPermissions(userId);

                if (user == null || Boolean.FALSE.equals(user.getActive())) {
                    throw ExceptionFactory.unauthorized("User not authorized");
                }


                var principal = AuthUser.fromUser(user);

                var authentication = new UsernamePasswordAuthenticationToken(principal, null,
                        principal.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ApiException ex) {
                SecurityContextHolder.clearContext();
                throw ex;
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                throw ExceptionFactory.unauthorized("User not authorized");
            }
        }

        chain.doFilter(req, res);
    }
}
