package ru.development.Deal.validation.custom_constraint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import ru.development.Deal.validation.custom_annotations.SalaryValidation;

import java.math.BigDecimal;

public class SalaryValidator implements ConstraintValidator<SalaryValidation, BigDecimal> {
    @Value("${minSalaryOfficial}")
    private BigDecimal minSalaryOfficial;

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value.compareTo(minSalaryOfficial) >= 0;
    }
}
