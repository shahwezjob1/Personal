package com.example.profile.repository;

import com.example.profile.domain.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Integer> {
    public Mono<Profile> findByEmail(String email);
}
