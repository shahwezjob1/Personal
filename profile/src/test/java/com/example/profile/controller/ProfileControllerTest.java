package com.example.profile.controller;

import static org.mockito.Mockito.*;

import com.example.profile.dto.ProfileDto;
import com.example.profile.exception.NotFoundException;
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

    public ProfileControllerTest(){
      log.info("constructor");
    }

    @BeforeAll
    public static void setup(@Value("${data.number}") String number, @Value("${data.profile1.email}") String email1,
                             @Value("${data.profile1.dob}") String dob1, @Value("${data.profile1.name}") String name1,
                             @Value("${EXCEPTION.NOT_FOUND}") String notFoundMessage, @Value("${MESSAGE.SUCCESS.GET}") String getSuccessMessage){
        ProfileControllerTest.email1 = email1;
        ProfileControllerTest.name1 = name1;
        ProfileControllerTest.dob1 = LocalDate.parse(dob1);
        ProfileControllerTest.number = number;
        ProfileControllerTest.getSuccessMessage = getSuccessMessage;
        ProfileControllerTest.notFoundMessage = notFoundMessage;
    }

    @Test
    void checkGetProfile() throws Exception {
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
    void checkGetProfileNotFound() throws Exception {
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
    void checkGetProfileException() throws Exception {
        when(profileService.getProfile(email1)).thenReturn(Mono.error(new Exception("exception")));
        webTestClient.get().uri("/profile/get/"+ email1).exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .jsonPath("$.message").isEqualTo("exception")
                .jsonPath("$.data").exists();
        verify(profileService, times(1)).getProfile(email1);
    }
}
