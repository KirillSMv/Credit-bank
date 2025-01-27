package ru.development.calculator.validation.custom_constraint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.development.calculator.validation.custom_annotations.PassportDateValidation;

import java.time.LocalDate;

public class PassportDateValidator implements ConstraintValidator<PassportDateValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isBefore(LocalDate.now()) && value.isAfter(LocalDate.EPOCH);
    }
}
