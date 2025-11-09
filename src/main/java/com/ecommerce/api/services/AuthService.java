package com.ecommerce.api.services;


import com.ecommerce.api.dto.AuthResponse;
import com.ecommerce.api.dto.LoginUserRequest;
import com.ecommerce.api.dto.RegisterUserRequest;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.utils.JwtUtil;
import com.ecommerce.api.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Transactional
    public AuthResponse register(RegisterUserRequest request, RoleCodeType roleType) {
        final String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw ExceptionFactory.emailAlreadyExist("Email already exists");
        }

        var user = this.buildUserEntity(request);

        var role = roleRepository.findByRole(roleType.toString());

        if (role == null) {
            throw ExceptionFactory.roleNotFound("Role not found");
        }

        user.setRoles(Set.of(role));

        userRepository.save(user);

        var authUser = AuthUser.fromUser(user);

        String token = jwtUtil.generateToken(authUser);

        return new AuthResponse(token, jwtUtil.calculateExpiration());
    }


    @Transactional
    public AuthResponse login(LoginUserRequest request) {
        final String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        var user = userRepository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw ExceptionFactory.invalidCredentials("The provided credentials are invalid");
        }

        if (!user.getActive()) {
            throw ExceptionFactory.invalidCredentials("The provided credentials are invalid");
        }

        if (user.checkPassword(request.getPassword(), passwordEncoder)) {
            throw ExceptionFactory.invalidCredentials("The provided credentials are invalid");
        }

        userRepository.save(user);

        var authUser = AuthUser.fromUser(user);

        String token = jwtUtil.generateToken(authUser);

        return new AuthResponse(token, jwtUtil.calculateExpiration());
    }


    public UserEntity buildUserEntity(RegisterUserRequest request) {
        var user = new UserEntity();
        user.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));
        user.setPassword(request.getPassword(), passwordEncoder);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(true);
        user.setCreatedAt(OffsetDateTime.now());
        return user;
    }
}
