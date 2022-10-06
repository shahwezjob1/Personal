package com.example.profile.annotation;

import com.example.profile.validator.NotAllNullConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAllNullConstraintValidator.class)
public @interface NotAllNull {
    String message() default "All Values Are Null Nothing To Update";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
