package com.example.profile.annotation;

import com.example.profile.validator.MinAgeDOBValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeDOBValidator.class)
public @interface MinAgeDOB {
    String message() default "DOB should not be contrary to Minimum Age. Minimum Age is {minimumAge} years old.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long minimumAge() default 18;
}

