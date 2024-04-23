package dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static enums.Roles.ROLE_PUBLIC;

public class Authentication implements org.springframework.security.core.Authentication {

    private final Map<String, Object> data;
    private final String token;
    private Boolean authenticated = false;

    public Authentication(Map<String, Object> data, String token) {
        this.data = data;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = (String) Optional.ofNullable(data.get("role")).orElse(ROLE_PUBLIC.name());
        return List.of(new SimpleGrantedAuthority(role));
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

    public Map<String, Object> getData() {
        return data;
    }
}
