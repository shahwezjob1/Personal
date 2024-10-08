package com.example.profile.util;

import com.example.profile.domain.Profile;
import com.example.profile.dto.ProfileDto;

public class ProfileUtil {
    private ProfileUtil(){};
    public static ProfileDto domainToDto(Profile profile){
        return new ProfileDto(profile.getEmail(), profile.getName(), profile.getDob(), profile.getNumber());
    }

    public static Profile dtoToDomain(ProfileDto profileDto, Profile profile){
        if(profileDto.getDob()!=null) {
            profile.setDob(profileDto.getDob());
        }
        if(profileDto.getName()!=null) {
            profile.setName(profileDto.getName());
        }
        if(profileDto.getNumber()!=null) {
            profile.setNumber(profileDto.getNumber());
        }
        return profile;
    }
}
