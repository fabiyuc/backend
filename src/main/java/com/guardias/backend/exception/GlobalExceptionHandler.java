package com.guardias.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.guardias.backend.dto.Mensaje;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Mensaje> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(new Mensaje("Null pointer exception occurred: " + e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mensaje> handleException(Exception e) {
        return new ResponseEntity<>(new Mensaje("An unexpected error occurred: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
