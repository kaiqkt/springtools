package com.kaiqkt.springtools.security.filters;

import com.kaiqkt.springtools.security.dto.Authentication;
import com.kaiqkt.springtools.security.handlers.AuthenticationHandler;
import com.kaiqkt.springtools.security.handlers.RestAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationHandler authenticationHandler;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public AuthenticationFilter(AuthenticationHandler authenticationHandler, RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationHandler = authenticationHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");
            Authentication authentication;

            if (token.isEmpty()) {
                authentication = authenticationHandler.handlePublic();
            } else if (token.startsWith("Bearer ")) {
                authentication = authenticationHandler.handleJWTToken(token.replace("Bearer ", ""));
            } else {
                authentication = authenticationHandler.handleAccessToken(token);
            }

            System.out.println(authentication.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }  catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, ex);
        }
    }
}
