package com.example.profile.service;

import com.example.profile.dto.ProfileDto;
import com.example.profile.exception.NotFoundException;
import com.example.profile.exception.PartialDataException;
import com.example.profile.repository.ProfileRepository;
import com.example.profile.util.ProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Mono<ProfileDto> getProfile(String email) {
        log.info(String.format("ProfileServiceImpl.getProfile(%s)", email));
        return profileRepository
                .findByEmail(email)
                .map(ProfileUtil::domainToDto)
                .switchIfEmpty(Mono.defer( () -> Mono.error(new NotFoundException())));
    }

    @Override
    public Flux<ProfileDto> getAll() {
        log.info("ProfileServiceImpl.getAll()");
        return profileRepository
                .findAll()
                .map(ProfileUtil::domainToDto)
                .switchIfEmpty(Mono.defer( () -> Mono.error(new NotFoundException())));
    }

    @Override
    public Mono<Boolean> putProfile(ProfileDto profileDto) {
        log.info(String.format("ProfileServiceImpl.putProfile(%s)", profileDto.getEmail()));
        return profileRepository
                .findByEmail(profileDto.getEmail())
                .switchIfEmpty(Mono.defer( () -> Mono.error(new NotFoundException())))
                .flatMap(profile -> {
                    if(profile.getName()==null && profile.getDob()==null && (profileDto.getName()==null || profileDto.getDob()==null)) {
                        return Mono.error(new PartialDataException());
                    } else {
                        return profileRepository
                                .save(ProfileUtil.dtoToDomain(profileDto, profile))
                                .thenReturn(true);
                    }
                });
    }
}
