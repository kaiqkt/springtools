package exceptions;

import enums.ErrorType;
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
