package com.ecommerce.api.utils;


import com.ecommerce.api.model.AuthUser;
import io.jsonwebtoken.Claims;

public class AuthUserJwtUtils extends JwtUtil<AuthUser> {
    @Override
    protected AuthUser createPayloadInstance(Claims claims) {
        AuthUser authUser = new AuthUser();
        authUser.fromMap(claims);
        return authUser;
    }
}
