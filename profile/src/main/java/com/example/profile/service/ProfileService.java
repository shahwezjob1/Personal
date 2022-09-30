package com.example.profile.service;

import com.example.profile.domain.Profile;
import com.example.profile.dto.ProfileDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileService {
    public Mono<ProfileDto> getProfile(String email);
    public Flux<ProfileDto> getAll();
}
