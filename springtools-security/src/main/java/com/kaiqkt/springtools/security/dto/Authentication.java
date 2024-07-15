package com.kaiqkt.springtools.security.dto;

import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kaiqkt.springtools.security.enums.Roles.ROLE_PUBLIC;

public class Authentication implements org.springframework.security.core.Authentication {

    private final Map<String, Object> data;
    private String token = "TOKEN";
    private Boolean authenticated = true;

    public Authentication(Map<String, Object> data, String token) {
        this.data = data;
        this.token = token;
    }

    public Authentication(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Object roles = data.get("roles");
        if (roles instanceof Claim) {
            System.out.println(((Claim) roles).asList(String.class));
        }

        return List.of(new SimpleGrantedAuthority(ROLE_PUBLIC.name()));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return data;
    }

    @Override
    public Object getPrincipal() {
        return "PRINCIPAL";
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return "PRINCIPAL";
    }

    public  Map<String, Object> getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "data=" + data +
                ", authenticated=" + authenticated +
                '}';
    }
}
