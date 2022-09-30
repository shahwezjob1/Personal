package com.example.profile.service;

import com.example.profile.dto.ProfileDto;
import com.example.profile.exception.NotFoundException;
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
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Flux<ProfileDto> getAll() {
        log.info(String.format("ProfileServiceImpl.getAll()"));
        return profileRepository
                .findAll()
                .map(ProfileUtil::domainToDto)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }
}
