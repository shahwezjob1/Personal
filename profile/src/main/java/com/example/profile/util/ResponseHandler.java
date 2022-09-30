package com.example.profile.util;

import com.example.profile.dto.ProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.LinkedHashMap;

@Slf4j
public class ResponseHandler {
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String DATA = "data";

    private ResponseHandler() {
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus httpStatus, String message, Object object) {
        log.info(String.format("Generating Response with Status : %s and Message : %s", httpStatus.toString(), message));
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(STATUS, httpStatus.toString());
        linkedHashMap.put(MESSAGE, message);
        if (object != null) {
            linkedHashMap.put(DATA, object);
        }
        return new ResponseEntity<Object>(linkedHashMap, httpStatus);
    }

}
