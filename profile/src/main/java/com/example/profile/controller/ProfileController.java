package com.example.profile.controller;

import com.example.profile.dto.ProfileDto;
import com.example.profile.service.ProfileService;
import com.example.profile.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = {"localhost:3000"})
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Value("${MESSAGE.SUCCESS.GET}")
    private String GET_SUCCESS_MESSAGE;
    @Value("${MESSAGE.FAILURE.GET}")
    private String GET_FAILURE_MESSAGE;

    @GetMapping("/get/{email}")
    public Mono<ResponseEntity<Object>> getProfile(@PathVariable("email") String email) {
        log.info(String.format("ProfileController.getProfile(%s)", email));
        return profileService
                .getProfile(email)
                .map(profileDto -> ResponseHandler.generateResponse(HttpStatus.FOUND, GET_SUCCESS_MESSAGE, profileDto));
    }

    @GetMapping(value = "/get/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProfileDto> getAll(){
        log.info(String.format("ProfileController.getAll()"));
        return profileService.getAll() ;
    }
}
