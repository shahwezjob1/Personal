package com.example.profile;

import com.example.profile.controller.ProfileController;
import com.example.profile.repository.ProfileRepository;
import com.example.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProfileApplicationTests {

	@Autowired
	ProfileController profileController;
	@Autowired
	ProfileService profileService;
	@Autowired
	ProfileRepository profileRepository;

	@Test
	void contextLoads() {
		assertThat(profileController).isNotNull();
		assertThat(profileService).isNotNull();
		assertThat(profileRepository).isNotNull();
	}

}
