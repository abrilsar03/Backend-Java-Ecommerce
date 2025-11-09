// services/UserService.java
package com.ecommerce.api.services;

import com.ecommerce.api.dto.ProfileResponse;
import com.ecommerce.api.dto.updateUserRequest;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponse findUser(UUID userId) {
        UserEntity user =
                userRepository.findById(userId).orElseThrow(() -> ExceptionFactory.userNotFound());


        return parseResponse(user);
    }

    @Transactional
    public ProfileResponse updateUser(UUID userId, updateUserRequest req) {
        UserEntity user =
                userRepository.findById(userId).orElseThrow(() -> ExceptionFactory.userNotFound());


        if (req.getFirstName() != null) {
            user.setFirstName(req.getFirstName());
        }

        if (req.getLastName() != null) {
            user.setLastName(req.getLastName());
        }

        if (req.getPhoneCode() != null) {
            user.setPhoneCode(req.getPhoneCode());
        }

        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }


        if (req.getAddress() != null) {
            user.setAddress(req.getAddress());
        }

        var updatedUser = userRepository.save(user);
        return parseResponse(updatedUser);
    }

    private ProfileResponse parseResponse(UserEntity user) {
        var dto = new ProfileResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneCode(user.getPhoneCode());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        return dto;
    }
}
