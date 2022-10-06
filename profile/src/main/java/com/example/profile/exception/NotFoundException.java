package com.example.profile.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class NotFoundException extends RuntimeException{
    private static final String MESSAGE = "REQUESTED RESOURCE NOT FOUND";

    public NotFoundException(){
        log.info("NotFoundException");
    }

    @Override
    public String getMessage(){
        return MESSAGE;
    }
}
