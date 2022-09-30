package com.example.profile.util;

import com.example.profile.domain.Profile;
import com.example.profile.dto.ProfileDto;

public class ProfileUtil {
    private ProfileUtil(){};
    public static ProfileDto domainToDto(Profile profile){
        return new ProfileDto(profile.getEmail(), profile.getName(), profile.getDob(), profile.getNumber());
    }
}
