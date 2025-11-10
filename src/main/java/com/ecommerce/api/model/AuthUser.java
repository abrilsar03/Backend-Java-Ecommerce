package com.ecommerce.api.model;

import com.ecommerce.api.entities.PermissionEntity;
import com.ecommerce.api.entities.RoleEntity;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.utils.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AuthUser extends JwtPayload implements UserDetails {

    private UUID id;

    private String email;
    private String firstName;
    private String lastName;

    private Set<RoleEntity> roles = new HashSet<>();
    private Set<PermissionEntity> directPermissions = new HashSet<>();

    public AuthUser() {

    }

    public AuthUser(UUID id, String email, String firstName, String lastName, Set<RoleEntity> roles,
            Set<PermissionEntity> directPermissions) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.directPermissions = directPermissions;
    }

    public static AuthUser fromUser(UserEntity user) {

        Set<RoleEntity> roles = Optional.ofNullable(user.getRoles()).orElseGet(Set::of);

        Set<PermissionEntity> directPermission =
                Optional.ofNullable(user.getDirectPermissions()).orElseGet(Set::of);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), roles, directPermission);

        return authUser;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id != null ? id.toString() : null);
        map.put("email", email);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        Object subject = map.get("sub");
        if (subject instanceof String s && !s.isBlank()) {
            try {
                this.id = UUID.fromString(s);
            } catch (Exception ignored) {
            }
        }

        if (this.id == null) {
            Object rawId = map.get("id");
            if (rawId instanceof String s && !s.isBlank()) {
                try {
                    this.id = UUID.fromString(s);
                } catch (Exception ignored) {
                }
            }
        }

        if (this.id == null) {
            Object rawUserId = map.get("userId");
            if (rawUserId instanceof String s && !s.isBlank()) {
                try {
                    this.id = UUID.fromString(s);
                } catch (Exception ignored) {
                }
            }
        }

        Object rawEmail = map.get("email");
        if (rawEmail instanceof String s)
            this.email = s;

        Object rawFirstName = map.get("firstName");
        if (rawFirstName instanceof String s)
            this.firstName = s;

        Object rawLastName = map.get("lastName");
        if (rawLastName instanceof String s)
            this.lastName = s;

        this.roles = new HashSet<>();
        this.directPermissions = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.stream().map(AuthorityUtils::convertRoleToAuthority).filter(Objects::nonNull)
                .forEach(authorities::add);

        directPermissions.stream().map(AuthorityUtils::convertPermissionToAuthority)
                .filter(Objects::nonNull).forEach(authorities::add);

        roles.forEach(role -> role.getPermissions().stream()
                .map(AuthorityUtils::convertPermissionToAuthority).filter(Objects::nonNull)
                .forEach(authorities::add));

        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<PermissionEntity> getDirectPermissions() {
        return directPermissions;
    }

    public void setDirectPermissions(Set<PermissionEntity> directPermissions) {
        this.directPermissions = directPermissions;
    }
}
