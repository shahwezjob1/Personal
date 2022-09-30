package com.example.profile;

//import com.example.profile.dto.ProfileDto;
//import com.example.profile.util.ResponseHandler;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class SystemTest {
//    private static final String EMAIL = "shahwezjob+1@gmail.com";
//    private static final String NUMBER = "7992394463";
//    private static final String NAME = "Shawaz";
//    private static final LocalDate DOB = LocalDate.parse("1999-03-13");
//    private static RestTemplate restTemplate;
//    private static ObjectMapper objectMapper;
//    private static ResponseEntity<Object> objectResponseEntity;
//    @Value("${MESSAGE.SUCCESS.GET}")
//    private String GET_SUCCESS_MESSAGE;
//    @Value("${MESSAGE.FAILURE.GET}")
//    private String GET_FAILURE_MESSAGE;
//
//    @BeforeAll
//    public static void setup() {
//        restTemplate = new RestTemplate();
//        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    }
//
//    @Test
//    public void check1() throws JsonProcessingException {
//        String url = "http://localhost:8080/profile/get/" + EMAIL;
//        String expected = objectMapper.writeValueAsString(
//                ResponseHandler.generateResponse(
//                                HttpStatus.FOUND,
//                                GET_SUCCESS_MESSAGE,
//                                new ProfileDto(EMAIL, NAME, DOB, NUMBER))
//                        .getBody()
//        ).replace("\"", "").replace(":", "=");
//        objectResponseEntity = restTemplate.getForEntity(url, Object.class);
//        assertEquals(HttpStatus.FOUND, objectResponseEntity.getStatusCode());
//        assertNotNull(objectResponseEntity.getBody());
//        assertEquals(expected, objectResponseEntity.getBody().toString().replace(", ", ","));
//    }
//
//    @Test
//    public void check2() throws JsonProcessingException, InterruptedException {
//        String url = "http://localhost:8080/profile/get/" + EMAIL.replace("1", "4");
//        String expected = objectMapper.writeValueAsString(
//                ResponseHandler.generateResponse(
//                                HttpStatus.NOT_FOUND,
//                                GET_FAILURE_MESSAGE,
//                                null)
//                        .getBody()
//        ).replace("\"", "").replace(":", "=");
//        assertThrows(HttpClientErrorException.NotFound.class, () -> {
//            objectResponseEntity = restTemplate.getForEntity(url, Object.class);
//            log.info(objectResponseEntity.toString());
//            assertEquals(HttpStatus.NOT_FOUND, objectResponseEntity.getStatusCode());
//            assertNotNull(objectResponseEntity.getBody());
//            assertEquals(expected, objectResponseEntity.getBody().toString().replace(", ", ","));
//        });
//    }
//}
