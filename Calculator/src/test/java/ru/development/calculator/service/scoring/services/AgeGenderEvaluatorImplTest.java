package ru.development.calculator.service.scoring.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.service.scoring.score_properties.AgeGenderScoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AgeGenderEvaluatorImplTest {
    @Mock
    private AgeGenderScoreProperties ageGenderScoreProperties;
    @InjectMocks
    private AgeGenderEvaluatorImpl ageGenderEvaluator;

    @Test
    void evaluateGenderWithAgeTest_whenAgeBelowMin_thenThrowScoringException() {
        Mockito.when(ageGenderScoreProperties.getMinClientAgeScoring()).thenReturn(20);
        Mockito.when(ageGenderScoreProperties.getMaxClientAgeScoring()).thenReturn(65);
        LocalDate birthday = LocalDate.of(2020, 12, 12);
        Gender gender = Gender.MALE;


        assertThrows(ScoringException.class, () -> ageGenderEvaluator.evaluateGenderWithAge(gender, birthday));
    }

    @Test
    void evaluateGenderWithAgeTest_whenFemaleWithinReliableAge_thenReturnResult() {
        Mockito.when(ageGenderScoreProperties.getMinClientAgeScoring()).thenReturn(20);
        Mockito.when(ageGenderScoreProperties.getMaxClientAgeScoring()).thenReturn(65);
        Mockito.when(ageGenderScoreProperties.getFemaleMinHighReliableAge()).thenReturn(32);
        Mockito.when(ageGenderScoreProperties.getFemaleMaxHighReliableAge()).thenReturn(60);
        Mockito.when(ageGenderScoreProperties.getFemaleReliableRangeCoef()).thenReturn(BigDecimal.valueOf(1));
        ReflectionTestUtils.setField(ageGenderEvaluator, "scoreAccuracy", 0);
        LocalDate birthday = LocalDate.of(1990, 12, 12);
        Gender gender = Gender.FEMALE;
        BigDecimal expectedResult = BigDecimal.valueOf(100);

        BigDecimal resultEvaluation = ageGenderEvaluator.evaluateGenderWithAge(gender, birthday);
        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateGenderWithAgeTest_whenMaleWithinReliableAge_thenReturnResult() {
        Mockito.when(ageGenderScoreProperties.getMinClientAgeScoring()).thenReturn(20);
        Mockito.when(ageGenderScoreProperties.getMaxClientAgeScoring()).thenReturn(65);
        Mockito.when(ageGenderScoreProperties.getMaleMinHighReliableAge()).thenReturn(30);
        Mockito.when(ageGenderScoreProperties.getMaleMaxHighReliableAge()).thenReturn(55);
        Mockito.when(ageGenderScoreProperties.getMaleReliableRangeCoef()).thenReturn(BigDecimal.valueOf(0.8));
        ReflectionTestUtils.setField(ageGenderEvaluator, "scoreAccuracy", 0);
        LocalDate birthday = LocalDate.of(1990, 12, 12);
        Gender gender = Gender.MALE;
        BigDecimal expectedResult = BigDecimal.valueOf(80);

        BigDecimal resultEvaluation = ageGenderEvaluator.evaluateGenderWithAge(gender, birthday);
        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateGenderWithAgeTest_whenNotBinary_thenReturnResult() {
        Mockito.when(ageGenderScoreProperties.getMinClientAgeScoring()).thenReturn(20);
        Mockito.when(ageGenderScoreProperties.getMaxClientAgeScoring()).thenReturn(65);
        Mockito.when(ageGenderScoreProperties.getNotBinaryGenderCoef()).thenReturn(BigDecimal.valueOf(0.3));
        ReflectionTestUtils.setField(ageGenderEvaluator, "scoreAccuracy", 0);
        LocalDate birthday = LocalDate.of(1990, 12, 12);
        Gender gender = Gender.NOTBINARY;
        BigDecimal expectedResult = BigDecimal.valueOf(30);

        BigDecimal resultEvaluation = ageGenderEvaluator.evaluateGenderWithAge(gender, birthday);
        assertEquals(expectedResult, resultEvaluation);
    }
}