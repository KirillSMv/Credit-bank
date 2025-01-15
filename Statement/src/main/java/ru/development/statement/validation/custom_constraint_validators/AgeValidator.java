package ru.development.statement.validation.custom_constraint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.development.statement.validation.custom_annotations.AgeValidation;

import java.time.LocalDate;

@Slf4j
public class AgeValidator implements ConstraintValidator<AgeValidation, LocalDate> {
    @Value("${ageOfMajority}")
    public Integer ageOfMajority;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isBefore(LocalDate.now().minusYears(ageOfMajority));
    }
}
