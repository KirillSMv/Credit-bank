package ru.development.calculator.service.scoring.services.interfaces;

import ru.development.calculator.model.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AgeGenderEvaluator {
    BigDecimal evaluateGenderWithAge(Gender gender, LocalDate birthdate);
}
