package com.ecommerce.api.utils;

import com.ecommerce.api.entities.PermissionEntity;
import com.ecommerce.api.entities.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class AuthorityUtils {

    private AuthorityUtils() {}

    public static GrantedAuthority convertRoleToAuthority(RoleEntity role) {
        if (role == null || role.getRole() == null) {
            return null;
        }

        String name = role.getRole().name();
        String authority = name.startsWith("ROLE_") ? name : "ROLE_" + name;
        return new SimpleGrantedAuthority(authority);
    }

    public static GrantedAuthority convertPermissionToAuthority(PermissionEntity permission) {
        if (permission == null || permission.getResource() == null
                || permission.getAction() == null) {
            return null;
        }
        String authority = permission.getResource().name() + ":" + permission.getAction().name();
        return new SimpleGrantedAuthority(authority);
    }

    public static String convertRoleToText(RoleEntity role) {
        if (role == null || role.getRole() == null) {
            return null;
        }

        String name = role.getRole().name();
        return name.startsWith("ROLE_") ? name : "ROLE_" + name;
    }

    public static String convertPermissionToText(PermissionEntity permission) {
        if (permission == null || permission.getResource() == null
                || permission.getAction() == null) {
            return null;
        }
        return permission.getResource().name() + ":" + permission.getAction().name();
    }

}
