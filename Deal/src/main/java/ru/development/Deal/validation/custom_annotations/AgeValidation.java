package ru.development.Deal.validation.custom_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.development.Deal.validation.custom_constraint_validators.AgeValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Constraint(validatedBy = AgeValidator.class)
public @interface AgeValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
