package com.example.profile.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@NoArgsConstructor
public class NotFoundException extends RuntimeException{
    //@Value("${EXCEPTION.NOT_FOUND}")
    private static String message = "REQUESTED RESOURCE NOT FOUND";

    @Override
    public String getMessage(){
        return message;
    }
}
