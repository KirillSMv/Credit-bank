package ru.development.Gateway.statement_service.validation.custom_constaint_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.development.Gateway.statement_service.validation.custom_annotations.MiddleNameValidation;

import java.util.regex.Pattern;

public class MiddleNameValidator implements ConstraintValidator<MiddleNameValidation, String> {
    private int minChars;
    private int maxChars;
    private static final Pattern LETTERS_CHECK = Pattern.compile("[a-zA-Z]+");

    @Override
    public void initialize(MiddleNameValidation constraintAnnotation) {
        this.minChars = constraintAnnotation.minChars();
        this.maxChars = constraintAnnotation.maxChars();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.length() >= minChars && value.length() <= maxChars && !value.isBlank() && LETTERS_CHECK.matcher(value).matches();
    }
}
