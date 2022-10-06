package com.example.profile.validator;

import com.example.profile.annotation.NotAllNull;
import com.example.profile.dto.ProfileDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotAllNullConstraintValidator implements ConstraintValidator<NotAllNull, ProfileDto> {
    @Override
    public boolean isValid(ProfileDto profileDto, ConstraintValidatorContext constraintValidatorContext) {
        if(profileDto.getDob()==null && profileDto.getNumber()==null && profileDto.getName()==null){
            return false;
        } else {
            return true;
        }
    }
}
