package com.example.profile.repository;

import com.example.profile.domain.Profile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@DataR2dbcTest
@ActiveProfiles({"test", "data"})
class RepoTest {
    private static Integer id3;
    private static String number;
    private static String email3;
    private static String password;
    private static String name3;
    private static String name4;
    private static LocalDate dob3;
    private static LocalDate dob4;
    private static Profile profile1;
    private static Profile profile2;
    @Autowired
    ProfileRepository profileRepository;

    public RepoTest(){
        log.info("constructor");
    }

    @BeforeAll
    public static void setup(@Value("${data.password}") String password, @Value("${data.number}") String number,
                             @Value("${data.profile3.id}") String id3, @Value("${data.profile3.email}") String email3,
                             @Value("${data.profile3.name}") String name3, @Value("${data.profile4.name}") String name4,
                             @Value("${data.profile3.dob}") String dob3, @Value("${data.profile4.dob}") String dob4) {
        RepoTest.password = password;
        RepoTest.number = number;
        RepoTest.id3 = Integer.parseInt(id3);
        RepoTest.email3 = email3;
        RepoTest.name3 = name3;
        RepoTest.name4 = name4;
        RepoTest.dob3 = LocalDate.parse(dob3);
        RepoTest.dob4 = LocalDate.parse(dob4);
        profile1 = new Profile(null, RepoTest.email3, RepoTest.password, RepoTest.name3, RepoTest.dob3, RepoTest.number);
        profile2 = new Profile(RepoTest.id3, RepoTest.email3, RepoTest.password, RepoTest.name4, RepoTest.dob4, RepoTest.number);
    }

    @Test
    void checkSaveNew() throws InterruptedException {
        profileRepository.save(profile1).as(StepVerifier::create).consumeNextWith(profile -> {
            assertEquals(id3, profile.getId());
            assertEquals(email3, profile.getEmail());
            assertEquals(password, profile.getPassword());
            assertEquals(name3, profile.getName());
            assertEquals(dob3, profile.getDob());
            assertEquals(number, profile.getNumber());
        }).verifyComplete();
        profileRepository.findByEmail(email3).as(StepVerifier::create).consumeNextWith(profile -> {
            assertEquals(id3, profile.getId());
            assertEquals(email3, profile.getEmail());
            assertEquals(password, profile.getPassword());
            assertEquals(name3, profile.getName());
            assertEquals(dob3, profile.getDob());
            assertEquals(number, profile.getNumber());
        }).verifyComplete();
    }


    @Test
    void checkSaveOld() throws InterruptedException {
        profileRepository.save(profile2).as(StepVerifier::create).consumeNextWith(profile -> {
            assertEquals(id3, profile.getId());
            assertEquals(email3, profile.getEmail());
            assertEquals(password, profile.getPassword());
            assertEquals(name4, profile.getName());
            assertEquals(dob4, profile.getDob());
            assertEquals(number, profile.getNumber());
        }).verifyComplete();
        profileRepository.findByEmail(email3).as(StepVerifier::create).consumeNextWith(profile -> {
            assertEquals(id3, profile.getId());
            assertEquals(email3, profile.getEmail());
            assertEquals(password, profile.getPassword());
            assertEquals(name4, profile.getName());
            assertEquals(dob4, profile.getDob());
            assertEquals(number, profile.getNumber());
        }).verifyComplete();
    }
}
