package com.ecommerce.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmailIgnoreCase(String email);

  UserEntity findByPhoneAndPhoneCode(String phone, String phoneCode);

  boolean existsByEmailIgnoreCase(String email);

  boolean existsByPhoneAndPhoneCode(String phone, String phoneCode);

  @Query("""
        select user from UserEntity user
        left join fetch user.roles role
        left join fetch role.permissions rp
        left join fetch user.directPermissions dp
        where user.id = :id
      """)

  UserEntity findByIdWithRolesAndPermissions(@Param("id") UUID id);

}

