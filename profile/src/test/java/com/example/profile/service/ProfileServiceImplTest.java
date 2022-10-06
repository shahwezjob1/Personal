package com.example.profile.service;

import com.example.profile.domain.Profile;
import com.example.profile.dto.ProfileDto;
import com.example.profile.exception.NotFoundException;
import com.example.profile.exception.PartialDataException;
import com.example.profile.repository.ProfileRepository;
import com.example.profile.util.ProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith({MockitoExtension.class})
class ProfileServiceImplTest {
    //@Value("${data.profile3.email}")
    private static String email3 = "shahwezjob+3@gmail.com";
    //@Value("${data.profile3.id}")
    private static Integer id3 = 3;
    //@Value("${data.profile3.dob}")
    private static LocalDate dob3 = LocalDate.parse("2000-01-01");
    //@Value("${data.profile3.name}")
    private static String name3 = "Mohammad Shahnawaz";
    //@Value("${data.number}")
    private static String number = "7992394463";
    //@Value("${data.password}")
    private static String password = "password";
    @Mock
    private ProfileRepository profileRepository;
    @InjectMocks
    private ProfileServiceImpl profileService;

    public ProfileServiceImplTest() {
        log.info("constructor");
    }

//    public ProfileServiceImplTest(@Value("${data.password}") String password, @Value("${data.number}") String number,
//                             @Value("${data.profile3.id}") String id3, @Value("${data.profile3.email}") String email3,
//                             @Value("${data.profile3.name}") String name3, @Value("${data.profile3.dob}") String dob3) {
//        log.info(String.format("-----------------> %s, %s, %s, %s, %s, %s",password, number, id3, email3, name3, dob3));
//        ProfileServiceImplTest.dob3 = LocalDate.parse(dob3);
//        ProfileServiceImplTest.email3 = email3;
//        ProfileServiceImplTest.password = password;
//        ProfileServiceImplTest.number = number;
//        ProfileServiceImplTest.id3 = Integer.parseInt(id3);
//        ProfileServiceImplTest.name3 = name3;
//    }

    @Test
    void checkGetProfile() {
        Profile profile = new Profile(id3, email3, password, name3, dob3, number);
        Mono<Profile> profileMono = Mono.just(profile);
        when(profileRepository.findByEmail(email3)).thenReturn(profileMono);
        profileService.getProfile(email3).as(StepVerifier::create)
                .consumeNextWith(profileDto -> {
                    assertEquals(email3, profileDto.getEmail());
                    assertEquals(number, profileDto.getNumber());
                    assertEquals(name3, profileDto.getName());
                    assertEquals(dob3, profileDto.getDob());
                }).verifyComplete();
    }

    @Test
    void checkGetProfileNotFound() {
        when(profileRepository.findByEmail(email3)).thenReturn(Mono.empty());
        profileService.getProfile(email3).as(StepVerifier::create).expectError(NotFoundException.class).verify();
    }

    @Test
    void checkGetAll() {
        Profile profile = new Profile(id3, email3, password, name3, dob3, number);
        Flux<Profile> profileFlux = Flux.just(profile, profile, profile);
        ProfileDto profileDto = ProfileUtil.domainToDto(profile);
        when(profileRepository.findAll()).thenReturn(profileFlux);
        profileService.getAll().as(StepVerifier::create)
                .expectNextCount(3)
                .expectNext(profileDto)
                .expectNext(profileDto)
                .expectComplete();
    }

    @Test
    void checkGetAllNotFound() {
        when(profileRepository.findAll()).thenReturn(Flux.empty());
        profileService.getAll().as(StepVerifier::create).expectError(NotFoundException.class).verify();
    }

    @Test
    void checkPutNotFound() {
        when(profileRepository.findByEmail(email3)).thenReturn(Mono.empty());
        profileService.putProfile(new ProfileDto(email3, null, null, null))
                .as(StepVerifier::create)
                .expectError(NotFoundException.class)
                .verify();
        verify(profileRepository, times(1)).findByEmail(email3);
        verify(profileRepository, times(0)).save(any());
    }

    @Test
    void checkPutPartialData() {
        when(profileRepository.findByEmail(email3))
                .thenReturn(Mono.just(new Profile(id3, email3, password, null, null, number)));
        profileService.putProfile(new ProfileDto(email3, null, null, null))
                .as(StepVerifier::create)
                .expectError(PartialDataException.class)
                .verify();
        verify(profileRepository, times(1)).findByEmail(email3);
        verify(profileRepository, times(0)).save(any());
    }

    @Test
    void checkPutTrue() {
        when(profileRepository.findByEmail(email3))
                .thenReturn(Mono.just(new Profile(id3, email3, password, name3, dob3, number)));
        when(profileRepository.save(new Profile(id3, email3, password, "shah", dob3, number)))
                .thenReturn(Mono.just(new Profile(id3, email3, password, "shah", dob3, number)));
        profileService.putProfile(new ProfileDto(email3, "shah", null, null))
                .as(StepVerifier::create)
                .consumeNextWith(aBoolean -> {
                    assertEquals(true, aBoolean);
                })
                .verifyComplete();
        verify(profileRepository, times(1)).findByEmail(email3);
        verify(profileRepository, times(1)).save(any());
        verify(profileRepository, times(1)).save(new Profile(id3, email3, password, "shah", dob3, number));
    }
}
