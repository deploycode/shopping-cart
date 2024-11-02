package com.example.shopping_cart.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = false)
@Data
public class BusinessRuleException extends RuntimeException {

    private HttpStatus httpStatus;

    public BusinessRuleException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
