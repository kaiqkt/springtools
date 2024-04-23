package com.kaiqkt.springtools.security.filters;

import com.kaiqkt.springtools.security.handlers.RestAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component("accessDeniedFilter")
public class AccessDeniedFilter extends OncePerRequestFilter {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public AccessDeniedFilter(RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException ex) {
            authenticationEntryPoint.handle(request, response, ex);
        }
    }
}
