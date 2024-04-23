package com.kaiqkt.springtools.security.handlers;

import com.kaiqkt.springtools.security.dto.Error;
import com.kaiqkt.springtools.security.enums.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Error error = new Error(ErrorType.ACCESS_DENIED, "You do not have permission to access this resource.");

        response.setContentType("application/json");
        response.getWriter().write(error.toString());
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
