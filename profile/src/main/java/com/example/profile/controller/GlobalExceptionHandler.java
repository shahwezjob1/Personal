package com.example.profile.controller;

import com.example.profile.exception.NotFoundException;
import com.example.profile.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> notFound(Exception exception) {
        log.info(String.format("GlobalExceptionHandler.notFound(%s)", exception.getMessage()));
        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> exception(Exception exception) {
        log.info(String.format("GlobalExceptionHandler.exception(%s)", exception.getMessage()));
        return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
    }
}
