package com.example.profile.validator;

import com.example.profile.annotation.MinAgeDOB;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

@Slf4j
public class MinAgeDOBValidator implements ConstraintValidator<MinAgeDOB, LocalDate> {
    private long minimumAge;

    @Override
    public void initialize(MinAgeDOB constraintAnnotation) {
        this.minimumAge = constraintAnnotation.minimumAge();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext constraintValidatorContext) {
        if(dob==null){
            return true;
        } else {
            LocalDate now = LocalDate.now();
            long age = YEARS.between(dob, now);
            return age >= this.minimumAge;
        }
    }
}
