package com.kaiqkt.springtools.security.handlers;

import com.kaiqkt.springtools.security.dto.Error;
import com.kaiqkt.springtools.security.enums.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
    Error error = new Error(ErrorType.ACCESS_DENIED, "You are not authorized to view this resource.");
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }
}
