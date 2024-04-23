package com.kaiqkt.springtools.security.dto;

import com.kaiqkt.springtools.security.enums.ErrorType;

public class Error {
    private final ErrorType type;
    private final String message;

    public Error(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return  """
                {
                    "type": "%s",
                    "message": "%s"
                }
                """.formatted(type, message);
    }
}
