package com.kaiqkt.springtools_security.utils;

import com.kaiqkt.springtools_security.dto.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

public class Context {
    public static Optional<Object> getValue(String key) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = (Authentication) context.getAuthentication();
        Object value = authentication.getData().get(key);

        return Optional.ofNullable(value);
    }

    public static Optional<Map<String, Object>> getData() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = (Authentication) context.getAuthentication();

        return Optional.ofNullable(authentication.getData());
    }
}
