package com.example.profile;

import com.example.profile.controller.ProfileController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles({"test", "strings", "data"})
public class IntegrationTest {
    @Autowired
    ProfileController profileController;
    @Autowired
    WebTestClient webTestClient;
    private static String email1 = "shahwezjob+1@gmail.com";
    private static String number = "7992394463";
    private static String name1 = "Shahwez";
    private static LocalDate dob1 = LocalDate.parse("1999-03-13");
    private static String getSuccessMessage;
    private static String notFoundMessage;

    public IntegrationTest(){
        log.info("constructor");
    }

    @BeforeAll
    public static void setup(@Value("${data.number}") String number, @Value("${data.profile1.email}") String email1,
                             @Value("${data.profile1.dob}") String dob1, @Value("${data.profile1.name}") String name1,
                             @Value("${EXCEPTION.NOT_FOUND}") String notFoundMessage, @Value("${MESSAGE.SUCCESS.GET}") String getSuccessMessage){
        IntegrationTest.dob1 = LocalDate.parse(dob1);
        IntegrationTest.number = number;
        IntegrationTest.name1 = name1;
        IntegrationTest.email1 = email1;
        IntegrationTest.getSuccessMessage = getSuccessMessage;
        IntegrationTest.notFoundMessage = notFoundMessage;
    }

    @Test
    public void check1() {
        webTestClient.get().uri("http://localhost:8080/profile/get/"+ email1)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.FOUND.toString())
                .jsonPath("$.message").isEqualTo(getSuccessMessage)
                .jsonPath("$.data.email").isEqualTo(email1)
                .jsonPath("$.data.name").isEqualTo(name1)
                .jsonPath("$.data.number").isEqualTo(number)
                .jsonPath("$.data.dob").isEqualTo(dob1.toString());
    }

    @Test
    public void check2() {
        webTestClient.get().uri("http://localhost:8080/profile/get/"+ email1.replace("1", "4"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.toString())
                .jsonPath("$.message").isEqualTo(notFoundMessage);
    }
}
