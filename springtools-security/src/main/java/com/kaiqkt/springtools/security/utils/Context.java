package com.kaiqkt.springtools.security.utils;

import com.auth0.jwt.interfaces.Claim;
import com.kaiqkt.springtools.security.dto.Authentication;
import com.kaiqkt.springtools.security.enums.ErrorType;
import com.kaiqkt.springtools.security.exceptions.UnauthorizedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

public class Context {
    public static <T> T getValue(String key, Class<T> T) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = (Authentication) context.getAuthentication();
        Claim claim = (Claim) authentication.getData().get(key);

        if (claim != null) {
            return claim.as(T);
        }

        throw new UnauthorizedException("Invalid JWT Token", ErrorType.INVALID_TOKEN);
    }

    public static Optional<Map<String, Object>> getData() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = (Authentication) context.getAuthentication();

        return Optional.ofNullable(authentication.getData());
    }
}
