package com.ecommerce.api.utils;

import com.ecommerce.api.entities.PermissionEntity;
import com.ecommerce.api.entities.RoleEntity;
import com.ecommerce.api.enums.PermissionActionType;
import com.ecommerce.api.enums.PermissionResourceType;
import com.ecommerce.api.enums.RoleCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthorityUtils Tests")
class AuthorityUtilsTest {

    private RoleEntity roleEntity;
    private PermissionEntity permissionEntity;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setRole(RoleCodeType.CLIENT);

        permissionEntity = new PermissionEntity();
        permissionEntity.setResource(PermissionResourceType.PRODUCT);
        permissionEntity.setAction(PermissionActionType.READ);
    }

    @Test
    @DisplayName("Should convert role to authority successfully")
    void testConvertRoleToAuthority_Success() {
        // Act
        GrantedAuthority result = AuthorityUtils.convertRoleToAuthority(roleEntity);

        // Assert
        assertNotNull(result);
        assertEquals("ROLE_CLIENT", result.getAuthority());
    }

    @Test
    @DisplayName("Should add ROLE_ prefix if not present")
    void testConvertRoleToAuthority_WithPrefix() {
        // Arrange
        roleEntity.setRole(RoleCodeType.ADMIN);

        // Act
        GrantedAuthority result = AuthorityUtils.convertRoleToAuthority(roleEntity);

        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthority().startsWith("ROLE_"));
    }

    @Test
    @DisplayName("Should return null for null role")
    void testConvertRoleToAuthority_NullRole() {
        // Act
        GrantedAuthority result = AuthorityUtils.convertRoleToAuthority(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null for role with null role code")
    void testConvertRoleToAuthority_NullRoleCode() {
        // Arrange
        roleEntity.setRole(null);

        // Act
        GrantedAuthority result = AuthorityUtils.convertRoleToAuthority(roleEntity);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should convert permission to authority successfully")
    void testConvertPermissionToAuthority_Success() {
        // Act
        GrantedAuthority result = AuthorityUtils.convertPermissionToAuthority(permissionEntity);

        // Assert
        assertNotNull(result);
        assertEquals("PRODUCT:READ", result.getAuthority());
    }

    @Test
    @DisplayName("Should return null for null permission")
    void testConvertPermissionToAuthority_NullPermission() {
        // Act
        GrantedAuthority result = AuthorityUtils.convertPermissionToAuthority(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null for permission with null resource")
    void testConvertPermissionToAuthority_NullResource() {
        // Arrange
        permissionEntity.setResource(null);

        // Act
        GrantedAuthority result = AuthorityUtils.convertPermissionToAuthority(permissionEntity);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should convert role to text successfully")
    void testConvertRoleToText_Success() {
        // Act
        String result = AuthorityUtils.convertRoleToText(roleEntity);

        // Assert
        assertNotNull(result);
        assertEquals("ROLE_CLIENT", result);
    }

    @Test
    @DisplayName("Should convert permission to text successfully")
    void testConvertPermissionToText_Success() {
        // Act
        String result = AuthorityUtils.convertPermissionToText(permissionEntity);

        // Assert
        assertNotNull(result);
        assertEquals("PRODUCT:READ", result);
    }
}

