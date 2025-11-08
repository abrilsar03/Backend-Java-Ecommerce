package com.ecommerce.api.services;


import com.ecommerce.api.dto.AuthResponse;
import com.ecommerce.api.dto.LoginUserRequest;
import com.ecommerce.api.dto.RegisterUserRequest;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.exceptions.ApiException;
import com.ecommerce.api.model.AuthUser;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.utils.JwtUtil;
import com.ecommerce.api.entities.RoleEntity;
import com.ecommerce.api.repositories.RoleRepository;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    public AuthResponse register(RegisterUserRequest request, RoleCodeType roleType)
            throws AuthException {
        final String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException("409-userEmailExists");
        }

        var user = this.buildUserEntity(request);

        var role = roleRepository.findByCode(roleType.toString());

        if (role == null) {
            throw new ApiException("409-roleNotFound");
        }

        user.setRoles(Set.of(role));

        userRepository.save(user);

        // Falta la logica del addres y del document

        var authUser = AuthUser.fromUser(user);

        String token = jwtUtil.generateToken(authUser);

        return new AuthResponse(token, jwtUtil.calculateExpiration());
    }


    @Transactional
    public AuthResponse login(LoginUserRequest request) {
        final String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        var user = userRepository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new ApiException("401-credentials");
        }

        if (!user.getActive()) {
            throw new ApiException("401-credentials");
        }

        if (user.checkPassword(request.getPassword(), passwordEncoder)) {
            throw new ApiException("401-credentials");
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
