package com.example.profile.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartialDataException extends RuntimeException{
    private static final String MESSAGE = "NEW PROFILE CANNOT BE PARTIALLY UPDATED";

    public PartialDataException() {
        log.info("PartialDataException");
    }

    @Override
    public String getMessage(){
        return MESSAGE;
    }
}
