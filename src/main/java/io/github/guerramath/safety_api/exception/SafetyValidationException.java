package io.github.guerramath.safety_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SafetyValidationException extends RuntimeException {
    public SafetyValidationException(String message) {
        super(message);
    }
}