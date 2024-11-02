package com.example.shopping_cart.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> handleBusinessRuleException(BusinessRuleException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
}
