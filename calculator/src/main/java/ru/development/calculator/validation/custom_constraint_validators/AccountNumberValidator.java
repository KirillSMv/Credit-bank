package ru.development.calculator.validation.custom_constraint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.development.calculator.validation.custom_annotations.AccountNumberValidation;

import java.util.regex.Pattern;

public class AccountNumberValidator implements ConstraintValidator<AccountNumberValidation, String> {
    private static final Pattern NUMBERS_CHECK = Pattern.compile("\\d+");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return NUMBERS_CHECK.matcher(value).matches() && value.length() == 20;
    }

}
