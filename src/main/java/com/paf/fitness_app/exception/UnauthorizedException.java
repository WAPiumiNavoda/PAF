package com.paf.fitness_app.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
public class UnauthorizedException extends RuntimeException{

    private final String message;

    public String getMessage() {
        return this.message;
    }
}
