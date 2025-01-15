package ru.development.statement.validation.custom_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.development.statement.validation.custom_constraint_validators.MiddleNameValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Constraint(validatedBy = MiddleNameValidator.class)
public @interface MiddleNameValidation {
    int minChars() default 2;

    String message() default "";

    int maxChars() default 30;

    Class<?>[] groups() default {};

    Class<? extends Throwable> exception() default MethodArgumentNotValidException.class;

    Class<? extends Payload>[] payload() default {};
}
