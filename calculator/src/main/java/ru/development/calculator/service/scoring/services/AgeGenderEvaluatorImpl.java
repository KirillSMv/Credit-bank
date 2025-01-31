package ru.development.calculator.service.scoring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.service.scoring.score_properties.AgeGenderScoreProperties;
import ru.development.calculator.service.scoring.services.interfaces.AgeGenderEvaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgeGenderEvaluatorImpl implements AgeGenderEvaluator {

    private final AgeGenderScoreProperties ageGenderScoreProperties;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;

    @Override
    public BigDecimal evaluateGenderWithAge(Gender gender, LocalDate birthdate) {
        BigDecimal score = BigDecimal.valueOf(100);
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < ageGenderScoreProperties.getMinClientAgeScoring() || age > ageGenderScoreProperties.getMaxClientAgeScoring()) {
            log.warn("age = {} < {} or > {}", age, ageGenderScoreProperties.getMinClientAgeScoring(), ageGenderScoreProperties.getMaxClientAgeScoring());
            throw new ScoringException("К сожалению, вам отказано в кредите");
        }
        if (gender == Gender.FEMALE && age >= ageGenderScoreProperties.getFemaleMinHighReliableAge() &&
                age <= ageGenderScoreProperties.getFemaleMaxHighReliableAge()) {
            score = BigDecimal.valueOf(100).multiply(ageGenderScoreProperties.getFemaleReliableRangeCoef());
        }
        if (gender == Gender.MALE && age >= ageGenderScoreProperties.getMaleMinHighReliableAge() &&
                age <= ageGenderScoreProperties.getMaleMaxHighReliableAge()) {
            score = BigDecimal.valueOf(100).multiply(ageGenderScoreProperties.getMaleReliableRangeCoef());
        }
        if (gender == Gender.NOTBINARY) {
            score = BigDecimal.valueOf(100).multiply(ageGenderScoreProperties.getNotBinaryGenderCoef());
        }
        log.debug("calculated evaluateGenderWithAge, score = {}", score.setScale(0, RoundingMode.HALF_UP));
        return score.setScale(scoreAccuracy, RoundingMode.HALF_UP);
    }
}
