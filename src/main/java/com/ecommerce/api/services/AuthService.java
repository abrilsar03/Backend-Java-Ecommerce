package com.ecommerce.api.services;


import com.ecommerce.api.dto.auth.AuthResponse;
import com.ecommerce.api.dto.auth.LoginUserRequest;
import com.ecommerce.api.dto.auth.RegisterUserRequest;
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

        try {
            if (userRepository.existsByEmailIgnoreCase(email)) {
                throw ExceptionFactory.emailAlreadyExist("Email already exists");
            }

            var user = this.buildUserEntity(request);

            var role = roleRepository.findByRole(roleType);

            if (role == null) {
                throw ExceptionFactory.roleNotFound("Role not found");
            }

            var roles = Set.of(role);

            user.setRoles(roles);

            userRepository.save(user);

            var authUser = AuthUser.fromUser(user);

            String token = jwtUtil.generateToken(authUser);

            return new AuthResponse(token, jwtUtil.calculateExpiration());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    public AuthResponse login(LoginUserRequest request) {
        final String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        try {
            var user =
                    userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> ExceptionFactory
                            .invalidCredentials("The provided credentials are invalid"));

            if (Boolean.FALSE.equals(user.getActive())) {
                throw ExceptionFactory.invalidCredentials("The provided credentials are invalid");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw ExceptionFactory.invalidCredentials("The provided credentials are invalid");
            }

            var authUser = AuthUser.fromUser(user);

            String token = jwtUtil.generateToken(authUser);

            return new AuthResponse(token, jwtUtil.calculateExpiration());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public UserEntity buildUserEntity(RegisterUserRequest request) {
        return new UserEntity(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPassword(), passwordEncoder, request.getPhoneCode(), request.getPhone(),
                request.getAddress(), request.getDocumentNumber(), request.getDocumentType());
    }
}
