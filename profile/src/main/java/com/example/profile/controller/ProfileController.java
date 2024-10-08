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

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = {"localhost:3000"})
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Value("${MESSAGE.SUCCESS.GET}")
    private String GET_SUCCESS_MESSAGE;
    @Value("${MESSAGE.SUCCESS.PUT}")
    private String PUT_SUCCESS_MESSAGE;

    @GetMapping("/get/{email}")
    public Mono<ResponseEntity<Object>> getProfile(@PathVariable("email") String email) {
        log.info(String.format("ProfileController.getProfile(%s)", email));
        return profileService
                .getProfile(email)
                .map(profileDto -> ResponseHandler.generateResponse(HttpStatus.FOUND, GET_SUCCESS_MESSAGE, profileDto));
    }

    @GetMapping(value = "/get/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProfileDto> getAll(){
        log.info("ProfileController.getAll()");
        return profileService.getAll() ;
    }

    @PutMapping("/put")
    public Mono<ResponseEntity<Object>> putProfile(@RequestBody @Valid  ProfileDto profileDto){
        log.info(String.format("ProfileController.putProfile(%s)", profileDto.toString()));
        return profileService
                .putProfile(profileDto)
                .map(aBoolean -> ResponseHandler.generateResponse(HttpStatus.OK, PUT_SUCCESS_MESSAGE, null));
    }
}
