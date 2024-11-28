package ru.development.calculator.service.scoring.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.service.scoring.score_properties.EmploymentScoreProperties;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmploymentScoringEvaluatorImplTest {
    @Mock
    private EmploymentScoreProperties employmentScoreProperties;

    @InjectMocks
    private EmploymentScoringEvaluatorImpl employmentScoringEvaluator;

    private static EmploymentDto employmentDtoUnemployedWithOtherPos;
    private static EmploymentDto employmentDtoSelfEmployedWithOtherPos;
    private static EmploymentDto employmentDtoBusinessOwnerWithOtherPos;
    private static EmploymentDto employmentDtoEmployedTopManager;
    private static EmploymentDto employmentDtoWithTotalExpBelowLimit;
    private static EmploymentDto employmentDtoWithReliableTotalExp;

    @BeforeAll
    static void setUp() {
        employmentDtoUnemployedWithOtherPos = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.UNEMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        employmentDtoSelfEmployedWithOtherPos = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.SELFEMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        employmentDtoBusinessOwnerWithOtherPos = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.BUSINESSOWNER)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();


        employmentDtoEmployedTopManager = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.TOPMANAGER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        employmentDtoWithTotalExpBelowLimit = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.TOPMANAGER)
                .workExperienceTotal(10)
                .workExperienceCurrent(10).build();

        employmentDtoWithReliableTotalExp = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.TOPMANAGER)
                .workExperienceTotal(120)
                .workExperienceCurrent(120).build();

    }

    @Test
    void evaluateEmploymentDataTest_whenUnemployed_thenThrowScoringException() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        assertThrows(ScoringException.class, () -> employmentScoringEvaluator.evaluateEmploymentData(employmentDtoUnemployedWithOtherPos));
    }

    @Test
    void evaluateEmploymentDataTest_whenSelfEmployedWithOtherPos_thenReturnResult() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        when(employmentScoreProperties.getSelfEmployedCoef()).thenReturn(BigDecimal.valueOf(0.5));
        when(employmentScoreProperties.getOtherPositionCoef()).thenReturn(BigDecimal.valueOf(0.5));
        when(employmentScoreProperties.getMinTotalWorkExp()).thenReturn(18);
        when(employmentScoreProperties.getMinCurrentWorkExp()).thenReturn(3);
        when(employmentScoreProperties.getMinReliableTotalWorkExp()).thenReturn(60);
        when(employmentScoreProperties.getMinReliableCurrentWorkExp()).thenReturn(6);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(120);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(36);
        when(employmentScoreProperties.getReliableWorkExpCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getEmploymentStatusSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.41));
        when(employmentScoreProperties.getPositionSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        when(employmentScoreProperties.getWorkExperienceSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        BigDecimal expectedResult = BigDecimal.valueOf(65);

        BigDecimal resultEvaluation = employmentScoringEvaluator.evaluateEmploymentData(employmentDtoSelfEmployedWithOtherPos);

        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateEmploymentDataTest_whenBusinessOwnerWithOtherPos_thenReturnResult() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        when(employmentScoreProperties.getBusinessOwnerCoef()).thenReturn(BigDecimal.valueOf(0.7));
        when(employmentScoreProperties.getOtherPositionCoef()).thenReturn(BigDecimal.valueOf(0.5));
        when(employmentScoreProperties.getMinTotalWorkExp()).thenReturn(18);
        when(employmentScoreProperties.getMinCurrentWorkExp()).thenReturn(3);
        when(employmentScoreProperties.getMinReliableTotalWorkExp()).thenReturn(60);
        when(employmentScoreProperties.getMinReliableCurrentWorkExp()).thenReturn(6);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(120);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(36);
        when(employmentScoreProperties.getReliableWorkExpCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getEmploymentStatusSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.41));
        when(employmentScoreProperties.getPositionSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        when(employmentScoreProperties.getWorkExperienceSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        BigDecimal expectedResult = BigDecimal.valueOf(73);

        BigDecimal resultEvaluation = employmentScoringEvaluator.evaluateEmploymentData(employmentDtoBusinessOwnerWithOtherPos);

        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateEmploymentDataTest_whenEmployedTopManager_thenReturnResult() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        when(employmentScoreProperties.getEmployedCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getTopManagerCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getMinTotalWorkExp()).thenReturn(18);
        when(employmentScoreProperties.getMinCurrentWorkExp()).thenReturn(3);
        when(employmentScoreProperties.getMinReliableTotalWorkExp()).thenReturn(60);
        when(employmentScoreProperties.getMinReliableCurrentWorkExp()).thenReturn(6);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(120);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(36);
        when(employmentScoreProperties.getReliableWorkExpCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getEmploymentStatusSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.41));
        when(employmentScoreProperties.getPositionSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        when(employmentScoreProperties.getWorkExperienceSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        BigDecimal expectedResult = BigDecimal.valueOf(100);

        BigDecimal resultEvaluation = employmentScoringEvaluator.evaluateEmploymentData(employmentDtoEmployedTopManager);

        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateEmploymentDataTest_whenTotalExperienceBelowLimit_thenThrowScoringException() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        when(employmentScoreProperties.getTopManagerCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getEmployedCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getMinTotalWorkExp()).thenReturn(18);


        assertThrows(ScoringException.class, () -> employmentScoringEvaluator.evaluateEmploymentData(employmentDtoWithTotalExpBelowLimit));
    }

    @Test
    void evaluateEmploymentDataTest_whenTotalExperienceIsReliable_thenReturnResult() {
        ReflectionTestUtils.setField(employmentScoringEvaluator, "scoreAccuracy", 0);
        when(employmentScoreProperties.getEmployedCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getTopManagerCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getMinTotalWorkExp()).thenReturn(18);
        when(employmentScoreProperties.getMinCurrentWorkExp()).thenReturn(3);
        when(employmentScoreProperties.getMinReliableTotalWorkExp()).thenReturn(60);
        when(employmentScoreProperties.getMinReliableCurrentWorkExp()).thenReturn(6);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(120);
        when(employmentScoreProperties.getReliableTotalWorkExp()).thenReturn(36);
        when(employmentScoreProperties.getReliableWorkExpCoef()).thenReturn(BigDecimal.valueOf(1));
        when(employmentScoreProperties.getEmploymentStatusSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.41));
        when(employmentScoreProperties.getPositionSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        when(employmentScoreProperties.getWorkExperienceSubscoreRatio()).thenReturn(BigDecimal.valueOf(0.295));
        BigDecimal expectedResult = BigDecimal.valueOf(100);

        BigDecimal resultEvaluation = employmentScoringEvaluator.evaluateEmploymentData(employmentDtoWithReliableTotalExp);
        assertEquals(expectedResult, resultEvaluation);
    }
}