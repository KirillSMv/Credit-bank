package ru.development.Gateway.statement_service.validation.custom_constaint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import ru.development.Gateway.statement_service.validation.custom_annotations.AgeValidation;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<AgeValidation, LocalDate> {
    @Value("${ageOfMajority}")
    public Integer ageOfMajority;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isBefore(LocalDate.now().minusYears(ageOfMajority));
    }
}
