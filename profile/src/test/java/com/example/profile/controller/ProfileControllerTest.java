package com.example.profile.controller;

import static org.mockito.Mockito.*;

import com.example.profile.dto.ProfileDto;
import com.example.profile.exception.NotFoundException;
import com.example.profile.exception.PartialDataException;
import com.example.profile.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@ActiveProfiles({"strings", "data"})
@WebFluxTest(ProfileController.class)
class ProfileControllerTest {
    @MockBean
    ProfileService profileService;
    @Autowired
    private WebTestClient webTestClient;
    private static String email1;
    private static String name1;
    private static String number;
    private static LocalDate dob1;
    private static String getSuccessMessage;
    private static String notFoundMessage;
    private static String partialDataMessage;
    private static String putSuccessMessage;

    public ProfileControllerTest(){
      log.info("constructor");
    }

    @BeforeAll
    public static void setup(@Value("${data.number}") String number, @Value("${data.profile1.email}") String email1,
                             @Value("${data.profile1.dob}") String dob1, @Value("${data.profile1.name}") String name1,
                             @Value("${EXCEPTION.NOT_FOUND}") String notFoundMessage,
                             @Value("${MESSAGE.SUCCESS.GET}") String getSuccessMessage,
                             @Value("${EXCEPTION.PARTIAL_DATA}") String partialDataMessage,
                             @Value("${MESSAGE.SUCCESS.PUT}") String putSuccessMessage){
        ProfileControllerTest.email1 = email1;
        ProfileControllerTest.name1 = name1;
        ProfileControllerTest.dob1 = LocalDate.parse(dob1);
        ProfileControllerTest.number = number;
        ProfileControllerTest.getSuccessMessage = getSuccessMessage;
        ProfileControllerTest.notFoundMessage = notFoundMessage;
        ProfileControllerTest.partialDataMessage = partialDataMessage;
        ProfileControllerTest.putSuccessMessage = putSuccessMessage;
    }

    @Test
    void checkGetProfile() {
        ProfileDto profileDto = new ProfileDto(email1, name1, dob1, number);
        when(profileService.getProfile(email1)).thenReturn(Mono.just(profileDto));
        webTestClient.get().uri("/profile/get/"+ email1).exchange()
                .expectStatus().isFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.FOUND.toString())
                .jsonPath("$.message").isEqualTo(getSuccessMessage)
                .jsonPath("$.data.email").isEqualTo(email1)
                .jsonPath("$.data.name").isEqualTo(name1)
                .jsonPath("$.data.number").isEqualTo(number)
                .jsonPath("$.data.dob").isEqualTo(dob1.toString());
        verify(profileService, times(1)).getProfile(email1);
    }

    @Test
    void checkGetProfileNotFound() {
        when(profileService.getProfile(email1)).thenReturn(Mono.error(new NotFoundException()));
        webTestClient.get().uri("/profile/get/"+ email1).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.toString())
                .jsonPath("$.message").isEqualTo(notFoundMessage)
                .jsonPath("$.data").doesNotExist();
        verify(profileService, times(1)).getProfile(email1);
    }

    @Test
    void checkGetProfileException() {
        when(profileService.getProfile(email1)).thenReturn(Mono.error(new Exception("exception")));
        webTestClient.get().uri("/profile/get/"+ email1).exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .jsonPath("$.message").isEqualTo("exception")
                .jsonPath("$.data").exists();
        verify(profileService, times(1)).getProfile(email1);
    }

    @Test
    void checkPutProfileNotFound() {
        ProfileDto profileDto = new ProfileDto("abcd@gmail.com", "abcd", null, null);
        when(profileService.putProfile(profileDto)).thenReturn(Mono.error(new NotFoundException()));
        webTestClient.put().uri("/profile/put")
                .bodyValue(profileDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.toString())
                .jsonPath("$.message").isEqualTo(notFoundMessage)
                .jsonPath("$.data").doesNotExist();
        verify(profileService, times(1)).putProfile(profileDto);
    }

    @Test
    void checkPutProfilePartialData() {
        ProfileDto profileDto = new ProfileDto(email1, "shah", null, null);
        when(profileService.putProfile(profileDto)).thenReturn(Mono.error(new PartialDataException()));
        webTestClient.put().uri("/profile/put")
                .bodyValue(profileDto)
                .exchange()
                .expectStatus().isNotModified()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_MODIFIED.toString())
                .jsonPath("$.message").isEqualTo(partialDataMessage)
                .jsonPath("$.data").doesNotExist();
        verify(profileService, times(1)).putProfile(profileDto);
    }


    @Test
    void checkPutDataTrue() {
        ProfileDto profileDto = new ProfileDto(email1, "shah", LocalDate.parse("2000-10-10"), null);
        when(profileService.putProfile(profileDto)).thenReturn(Mono.just(true));
        webTestClient.put().uri("/profile/put")
                .body(BodyInserters.fromValue(profileDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.OK.toString())
                .jsonPath("$.message").isEqualTo(putSuccessMessage)
                .jsonPath("$.data").doesNotExist();
        verify(profileService, times(1)).putProfile(profileDto);
    }
}
