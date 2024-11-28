package ru.development.calculator.service.scoring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.service.scoring.score_properties.EmploymentScoreProperties;
import ru.development.calculator.service.scoring.services.interfaces.EmploymentScoringEvaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.development.calculator.model.enums.EmploymentStatus.UNEMPLOYED;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmploymentScoringEvaluatorImpl implements EmploymentScoringEvaluator {

    private final EmploymentScoreProperties employmentScoreProperties;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;

    @Override
    public BigDecimal evaluateEmploymentData(EmploymentDto employmentDto) {
        BigDecimal score;
        BigDecimal employmentStatusScore = evaluateEmploymentStatus(employmentDto.getEmploymentStatus());
        BigDecimal positionScore = evaluatePosition(employmentDto.getPositionType());
        BigDecimal workExperienceScore = evaluateWorkExperience(employmentDto.getWorkExperienceTotal(), employmentDto.getWorkExperienceCurrent());
        evaluateEmployer(employmentDto.getEmployerINN());
        score = employmentStatusScore.multiply(employmentScoreProperties.getEmploymentStatusSubscoreRatio())
                .add((positionScore).multiply(employmentScoreProperties.getPositionSubscoreRatio()))
                .add((workExperienceScore.multiply(employmentScoreProperties.getWorkExperienceSubscoreRatio())));
        log.debug("calculated evaluateEmploymentData score = {} ", score.setScale(0, RoundingMode.HALF_UP));
        return score.setScale(scoreAccuracy, RoundingMode.HALF_UP);
    }

    private BigDecimal evaluateEmploymentStatus(EmploymentStatus employmentStatus) {
        BigDecimal score = BigDecimal.valueOf(100);
        switch (employmentStatus) {
            case UNEMPLOYED -> {
                log.warn("метод evaluateEmploymentStatus, отказ по причине рабочего статуса: {}", UNEMPLOYED.getEmploymentStatus());
                throw new ScoringException("К сожалению, вам отказано в кредите");
            }
            case SELFEMPLOYED -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getSelfEmployedCoef());
            }
            case BUSINESSOWNER -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getBusinessOwnerCoef());
            }
            case EMPLOYED -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getEmployedCoef());
            }
        }
        log.debug("метод evaluateEmploymentStatus, subscore = {}", score);
        return score;
    }

    private BigDecimal evaluatePosition(PositionType positionType) {
        if (positionType == null) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal score = BigDecimal.valueOf(100);
        switch (positionType) {
            case MANAGERMIDDLELEVEL -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getManagerMiddleLevelCoef());
            }
            case TOPMANAGER -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getTopManagerCoef());
            }
            case OTHER -> {
                score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getOtherPositionCoef());
            }
        }
        log.debug("метод evaluatePosition, subscore = {}", score);
        return score;
    }

    private BigDecimal evaluateWorkExperience(Integer workExperienceTotal, Integer workExperienceCurrent) {
        if (workExperienceTotal < employmentScoreProperties.getMinTotalWorkExp() ||
                workExperienceCurrent < employmentScoreProperties.getMinCurrentWorkExp()) {
            log.warn("метод evaluateWorkExperience, отказ по причине недостаточного опыта работы, " +
                    "workExperienceTotal: {}, workExperienceCurrent: {}", workExperienceTotal, workExperienceCurrent);
            throw new ScoringException("К сожалению, вам отказано в кредите");
        }
        BigDecimal score;
        if (workExperienceTotal < employmentScoreProperties.getMinReliableTotalWorkExp() ||
                workExperienceCurrent < employmentScoreProperties.getMinReliableCurrentWorkExp()) {
            score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getLessMinReliableWorkExpCoef());
        } else if (workExperienceTotal > employmentScoreProperties.getReliableTotalWorkExp() &&
                workExperienceCurrent > employmentScoreProperties.getReliableCurrentWorkExp()) {
            score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getReliableWorkExpCoef());
        } else {
            score = BigDecimal.valueOf(100).multiply(employmentScoreProperties.getOtherWorkExpCoef());
        }
        log.debug("метод evaluateWorkExperience, subscore = {}", score);
        return score;
    }

    private void evaluateEmployer(String employerINN) {
        //имитируем проверку работодателя по внешним базам
        System.out.println("Работодатель с INN - " + employerINN + " существует");
    }

}
