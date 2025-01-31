package ru.development.gateway.model.validation.custom_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.development.gateway.model.validation.custom_constaint_validators.PassportDateValidator;

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
