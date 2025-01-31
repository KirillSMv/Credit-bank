package ru.development.calculator.service.scoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.scoring.score_properties.ScoringParametersRatioProperties;
import ru.development.calculator.service.scoring.services.interfaces.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoringServiceFICOImpl implements ScoringService {
    private final EmploymentScoringEvaluator employmentScoringEvaluator;
    private final CreditHistoryScoringEvaluator creditHistoryScoringEvaluator;
    private final ClientLoanBurdenEvaluator clientLoanBurdenEvaluator;
    private final FamilyParametersEvaluator familyParametersEvaluator;
    private final LoanAmountEvaluator loanAmountEvaluator;
    private final AgeGenderEvaluator ageGenderEvaluator;
    private final ScoringParametersRatioProperties scoringParametersRatioProperties;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;

    @Override
    public BigDecimal getScore(ScoringDataDto scoringDataDto) {
        BigDecimal scoringCoef = getScoringCoef(scoringDataDto);
        BigDecimal scoreFICO = BigDecimal.valueOf(300).add(scoringCoef.multiply(BigDecimal.valueOf(5.5)));
        return scoreFICO.setScale(scoreAccuracy, RoundingMode.HALF_UP);
    }

    private BigDecimal getScoringCoef(ScoringDataDto scoringDataDto) {
        EmploymentDto employmentDto = scoringDataDto.getEmployment();
        BigDecimal scoreCoef;
        BigDecimal employmentScore = employmentScoringEvaluator.evaluateEmploymentData(employmentDto);
        BigDecimal creditHistoryScore = creditHistoryScoringEvaluator.evaluateCreditHistory(scoringDataDto);
        BigDecimal clientLoanBurdenScore = clientLoanBurdenEvaluator.evaluateCurrentLoadBurden(scoringDataDto);
        BigDecimal familyParametersScore = familyParametersEvaluator.evaluateFamilyParameters(scoringDataDto);
        BigDecimal loanAmountScore = loanAmountEvaluator.evaluateAmountOfLoan(scoringDataDto);
        BigDecimal ageGenderScore = ageGenderEvaluator.evaluateGenderWithAge(scoringDataDto.getGender(), scoringDataDto.getBirthdate());
        scoreCoef = employmentScore.multiply(scoringParametersRatioProperties.getEmploymentScoreRatio())
                .add(creditHistoryScore.multiply(scoringParametersRatioProperties.getCreditHistoryScoreRatio()))
                .add(clientLoanBurdenScore.multiply(scoringParametersRatioProperties.getClientLoanBurdenScoreRatio()))
                .add(familyParametersScore.multiply(scoringParametersRatioProperties.getFamilyParametersScoreRatio()))
                .add(loanAmountScore.multiply(scoringParametersRatioProperties.getLoanAmountScoreRatio()))
                .add(ageGenderScore.multiply(scoringParametersRatioProperties.getAgeGenderScoreRatio()));
        log.debug("метод getScoringCoef, scoreCoef = {}", scoreCoef);
        return scoreCoef.setScale(scoreAccuracy, RoundingMode.HALF_UP);
    }
}