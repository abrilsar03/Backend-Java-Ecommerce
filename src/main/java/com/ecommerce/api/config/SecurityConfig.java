package com.ecommerce.api.config;

// SecurityConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.filters.JwtAuthFilter;
import com.ecommerce.api.utils.AuthUserJwtUtils;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthUserJwtUtils jwt;

    public SecurityConfig(AuthUserJwtUtils jwt) {
        this.jwt = jwt;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/health/**").permitAll().requestMatchers("/admin/**")
                        .hasRole(RoleCodeType.ADMIN.name()).requestMatchers("/users/**")
                        .hasAnyRole(RoleCodeType.ADMIN.name(), RoleCodeType.CLIENT.name())
                        .anyRequest().authenticated())

                .addFilterBefore(new JwtAuthFilter(jwt, null),
                        UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
