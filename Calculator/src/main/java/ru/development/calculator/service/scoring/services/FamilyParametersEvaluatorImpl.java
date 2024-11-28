package ru.development.calculator.service.scoring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.service.scoring.score_properties.FamilyParametersScoreProperties;
import ru.development.calculator.service.scoring.services.interfaces.FamilyParametersEvaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyParametersEvaluatorImpl implements FamilyParametersEvaluator {

    private final FamilyParametersScoreProperties familyParametersScoreProperties;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;

    @Override
    public BigDecimal evaluateFamilyParameters(ScoringDataDto scoringDataDto) {
        BigDecimal score;
        BigDecimal numberOfDependentScore = evaluateNumberOfDependents(scoringDataDto.getDependentAmount());
        BigDecimal maritalStatusScore = evaluateMaritalStatus(scoringDataDto.getMaritalStatus());
        score = numberOfDependentScore.multiply(familyParametersScoreProperties.getNumberOfDependentScoreRatio())
                .add((maritalStatusScore.multiply(familyParametersScoreProperties.getMaritalStatusScoreRatio()))).setScale(scoreAccuracy, RoundingMode.HALF_UP);
        log.debug("calculated evaluateFamilyParameters score = {}", score);
        return score;
    }

    private BigDecimal evaluateNumberOfDependents(Integer dependentAmount) {
        BigDecimal score = BigDecimal.valueOf(100);
        if (dependentAmount >= familyParametersScoreProperties.getHighNumberOfDependents()) {
            score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getHighNumberOfDependentsCoef());
        } else if (dependentAmount.equals(familyParametersScoreProperties.getMediumNumberOfDependents())) {
            score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getMediumNumberOfDependentsCoef());
        } else if (dependentAmount.equals(familyParametersScoreProperties.getLowNumberOfDependents())) {
            score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getLowNumberOfDependentsCoef());
        } else if (dependentAmount == 0) {
            score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getNoDependentCoef());
        }
        log.debug("метод evaluateNumberOfDependents, subscore = {}", score);
        return score;
    }

    private BigDecimal evaluateMaritalStatus(MaritalStatus maritalStatus) {
        BigDecimal score = BigDecimal.valueOf(100);
        switch (maritalStatus) {
            case MARRIED -> {
                score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getMarriedStatusCoef());
            }
            case DIVORCED -> {
                score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getDivorcedStatusCoef());
            }
            case SINGLE -> {
                score = BigDecimal.valueOf(100).multiply(familyParametersScoreProperties.getSingleStatusCoef());
            }
        }
        log.debug("метод evaluateMaritalStatus, subscore = {}", score);
        return score;
    }
}
