package com.ecommerce.api.config;

import com.ecommerce.api.enums.AuthorityType;
import com.ecommerce.api.filters.JwtAuthFilter;
import com.ecommerce.api.repositories.ApiKeyRepository;
import com.ecommerce.api.security.ApiKeyAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ApiKeyRepository apiKeyRepository;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, ApiKeyRepository apiKeyRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.apiKeyRepository = apiKeyRepository;

    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> body = Map.of("status", 401, "error", "Unauthorized", "message",
                    "Authentication required", "timestamp", Instant.now().toString());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            Map<String, Object> body = Map.of("status", 403, "error", "Forbidden", "message",
                    "Access denied", "timestamp", Instant.now().toString());
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        };
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))

                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/health-check/**", "/auth/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                                .requestMatchers("/tokenization/**")
                                .hasAuthority(AuthorityType.API_CLIENT.name())

                                .anyRequest().authenticated())


                .addFilterBefore(new ApiKeyAuth(apiKeyRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
