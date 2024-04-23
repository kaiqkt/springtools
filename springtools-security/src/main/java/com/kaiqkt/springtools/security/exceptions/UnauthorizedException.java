package com.kaiqkt.springtools.security.exceptions;

import com.kaiqkt.springtools.security.enums.ErrorType;
import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    private final ErrorType type;

    public UnauthorizedException(String msg, ErrorType type) {
        super(msg);
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}
