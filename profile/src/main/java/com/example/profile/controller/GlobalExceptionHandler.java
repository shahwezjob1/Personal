package com.example.profile.controller;

import com.example.profile.exception.NotFoundException;
import com.example.profile.exception.PartialDataException;
import com.example.profile.util.ResponseHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String NOT_UPDATED_MESS = "PROFILE NOT UPDATED";
    private static final String INVALID_DATE = "%s : DATE SHOULD BE VALID AND IN FORMAT yyyy-MM-dd";

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> notFound(Exception exception) {
        log.info(String.format("GlobalExceptionHandler.notFound(%s)", exception.getMessage()));
        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler({PartialDataException.class})
    public ResponseEntity<Object> partialData(Exception exception) {
        log.info(String.format("GlobalExceptionhandler.partialData(%s)", exception.getMessage()));
        return ResponseHandler.generateResponse(HttpStatus.NOT_MODIFIED, exception.getMessage(), null);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    public ResponseEntity<Object> invalidArguments(WebExchangeBindException exception) {
        log.info(String.format("GlobalExceptionHandler.invalidArguments(%s)", exception.getMessage()));
        LinkedHashMap<String, String > linkedHashMap = new LinkedHashMap<>();
        exception.getGlobalErrors().forEach(objectError -> linkedHashMap.put(objectError.getCode(), objectError.getDefaultMessage()));
        exception.getFieldErrors().forEach(fieldError -> linkedHashMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, NOT_UPDATED_MESS, linkedHashMap);
    }

    @ExceptionHandler({ServerWebInputException.class})
    public ResponseEntity<Object> invalidDate(ServerWebInputException exception) {
        String message = exception.getMessage();
        log.info(String.format("GlobalExceptionHandler.invalidDate(%s)", message));
        int offset = message.indexOf("(java.time.format.DateTimeParseException) Text \'");
        int begin = message.indexOf("\'", offset);
        int end = message.indexOf("\'", begin+1);
        if(begin>=0 && end<message.length()){
            String date = message.substring(begin+1, end);
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, String.format(INVALID_DATE, date), null);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, message, exception);
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> exception(Exception exception) {
        log.info(String.format("GlobalExceptionHandler.exception{%s}(%s)",exception.getClass().getName(), exception.getMessage()));
        return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
    }
}
