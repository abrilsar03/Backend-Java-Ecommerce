package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.api.entities.UserEntity;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmailIgnoreCase(String email);

    UserEntity findByPhoneAndPhoneCode(String phone, String phoneCode);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneAndPhoneCode(String phone, String phoneCode);
}

