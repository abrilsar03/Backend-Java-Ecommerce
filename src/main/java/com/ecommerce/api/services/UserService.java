// services/UserService.java
package com.ecommerce.api.services;

import com.ecommerce.api.dto.users.ProfileResponse;
import com.ecommerce.api.dto.users.UpdateUserRequest;
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
    public ProfileResponse updateUser(UUID userId, UpdateUserRequest req) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> ExceptionFactory.userNotFound());


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

            if (req.getDocumentNumber() != null) {
                user.setDocumentNumber(req.getDocumentNumber());
            }

            if (req.getDocumentType() != null) {
                user.setDocumentType(req.getDocumentType());
            }

            var updatedUser = userRepository.save(user);
            return parseResponse(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private ProfileResponse parseResponse(UserEntity user) {
        var userResponse = new ProfileResponse(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getPhoneCode(), user.getPhone(), user.getAddress(),
                user.getDocumentType(), user.getDocumentNumber());
        return userResponse;
    }
}
