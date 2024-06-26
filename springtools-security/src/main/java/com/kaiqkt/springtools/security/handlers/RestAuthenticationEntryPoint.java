package com.kaiqkt.springtools.security.handlers;

import com.kaiqkt.springtools.security.dto.Error;
import com.kaiqkt.springtools.security.enums.ErrorType;
import com.kaiqkt.springtools.security.exceptions.UnauthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = Optional.ofNullable(authException.getMessage()).orElse("Authentication Error");
        ErrorType errorType = ErrorType.AUTHENTICATION_ERROR;

        if (authException instanceof UnauthorizedException) {
            errorType = ((UnauthorizedException) authException).getType();
        }

        Error error = new Error(errorType, message);

        response.setContentType("application/json");
        response.getWriter().write(error.toString());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
