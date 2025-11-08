package com.ecommerce.api.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.ecommerce.api.entities.PermissionEntity;
import com.ecommerce.api.entities.RoleEntity;
import com.ecommerce.api.entities.UserEntity;
import com.ecommerce.api.enums.PermissionActionType;
import com.ecommerce.api.enums.PermissionResourceType;
import com.ecommerce.api.enums.RoleCodeType;
import com.ecommerce.api.utils.AuthorityUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;

public class AuthUser extends JwtPayload implements UserDetails {
    @NotNull
    @NotBlank
    private UUID id;

    private String email;
    private String firstName;
    private String lastName;
    private Set<RoleEntity> roles = new HashSet<>();
    private Set<PermissionEntity> directPermissions = new HashSet<>();

    public static AuthUser fromUser(UserEntity user) {
        AuthUser authUser = new AuthUser();
        authUser.id = user.getId();
        authUser.email = user.getEmail();
        authUser.firstName = user.getFirstName();
        authUser.lastName = user.getLastName();
        authUser.roles = user.getRoles() == null ? Set.of() : user.getRoles();
        authUser.directPermissions =
                user.getDirectPermissions() == null ? Set.of() : user.getDirectPermissions();
        return authUser;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id != null ? id.toString() : null);
        map.put("email", email);
        map.put("firstName", firstName);
        map.put("lastName", lastName);

        List<String> roleStrings = roles.stream().map(AuthorityUtils::convertRoleToText)
                .filter(Objects::nonNull).toList();

        Set<String> permissionStrings = new HashSet<>();
        directPermissions.stream().map(AuthorityUtils::convertPermissionToText)
                .filter(Objects::nonNull).forEach(permissionStrings::add);

        roles.forEach(
                role -> role.getPermissions().stream().map(AuthorityUtils::convertPermissionToText)
                        .filter(Objects::nonNull).forEach(permissionStrings::add));

        map.put("roles", roleStrings);
        map.put("permissions", List.copyOf(permissionStrings));
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fromMap(Map<String, Object> map) {
        Object rawId = map.get("id");
        this.id = (rawId instanceof String s && !s.isBlank()) ? UUID.fromString(s) : null;
        this.email = (String) map.get("email");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");

        this.roles = new HashSet<>();
        Object rawRoles = map.get("roles");
        if (rawRoles instanceof Collection<?> collection) {
            for (Object item : collection) {
                if (item instanceof String roleName && !roleName.isBlank()) {
                    RoleEntity roleEntity = new RoleEntity();
                    try {
                        String code =
                                roleName.startsWith("ROLE_") ? roleName.substring(5) : roleName;
                        roleEntity.setRole(RoleCodeType.valueOf(code));
                        this.roles.add(roleEntity);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }

        this.directPermissions = new HashSet<>();

        Object rawPermissions = map.get("permissions");
        if (rawPermissions instanceof Collection<?> collection) {
            for (Object item : collection) {
                if (item instanceof String permissionText && permissionText.contains(":")) {
                    String[] parts = permissionText.split(":", 2);
                    PermissionEntity permissionEntity = new PermissionEntity();
                    try {
                        permissionEntity.setResource(PermissionResourceType.valueOf(parts[0]));
                        permissionEntity.setAction(PermissionActionType.valueOf(parts[1]));
                        this.directPermissions.add(permissionEntity);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
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
}
