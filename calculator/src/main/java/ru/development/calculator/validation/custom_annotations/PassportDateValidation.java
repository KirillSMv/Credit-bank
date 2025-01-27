package ru.development.calculator.validation.custom_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.development.calculator.validation.custom_constraint_validators.PassportDateValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Constraint(validatedBy = PassportDateValidator.class)
public @interface PassportDateValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
