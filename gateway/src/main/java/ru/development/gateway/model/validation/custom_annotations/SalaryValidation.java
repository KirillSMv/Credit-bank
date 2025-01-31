package ru.development.gateway.model.validation.custom_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.development.gateway.model.validation.custom_constaint_validators.SalaryValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Constraint(validatedBy = SalaryValidator.class)
public @interface SalaryValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
