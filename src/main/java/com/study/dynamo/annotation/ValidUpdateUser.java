package com.study.dynamo.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidatorUpdateUser.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpdateUser {
    String message() default "Invalid update user request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
