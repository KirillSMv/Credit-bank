package ru.development.calculator.service.scoring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.service.scoring.score_properties.FamilyParametersScoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FamilyParametersEvaluatorImplTest {

    @Mock
    private FamilyParametersScoreProperties familyParametersScoreProperties;
    @InjectMocks
    private FamilyParametersEvaluatorImpl familyParametersEvaluator;

    private static EmploymentDto employmentDtoUnemployedWithOtherPos;
    private static ScoringDataDto scoringDataDtoWithNoDependentAndSingle;
    private static ScoringDataDto scoringDataDtoMarriedWithOneDependent;

    @BeforeAll
    static void setUp() {
        employmentDtoUnemployedWithOtherPos = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        scoringDataDtoWithNoDependentAndSingle = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(employmentDtoUnemployedWithOtherPos)
                .birthdate(LocalDate.of(1995, 12, 7))
                .passportSeries("4040")
                .passportNumber("111111")
                .passportIssueDate(LocalDate.of(2010, 7, 20))
                .passportIssueBranch("MVD2020")
                .dependentAmount(0)
                .accountNumber(null)
                .maritalStatus(MaritalStatus.SINGLE)
                .isInsuranceEnabled(false)
                .isSalaryClient(false).build();

        scoringDataDtoMarriedWithOneDependent = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(employmentDtoUnemployedWithOtherPos)
                .birthdate(LocalDate.of(1995, 12, 7))
                .passportSeries("4040")
                .passportNumber("111111")
                .passportIssueDate(LocalDate.of(2010, 7, 20))
                .passportIssueBranch("MVD2020")
                .dependentAmount(1)
                .accountNumber(null)
                .maritalStatus(MaritalStatus.MARRIED)
                .isInsuranceEnabled(false)
                .isSalaryClient(false).build();
    }

    @Test
    void evaluateFamilyParametersTest_whenZeroDependentAndSingle_thenReturnResult() {
        ReflectionTestUtils.setField(familyParametersEvaluator, "scoreAccuracy", 0);
        when(familyParametersScoreProperties.getNoDependentCoef()).thenReturn(BigDecimal.valueOf(1));
        when(familyParametersScoreProperties.getSingleStatusCoef()).thenReturn(BigDecimal.valueOf(0.7));
        when(familyParametersScoreProperties.getNumberOfDependentScoreRatio()).thenReturn(BigDecimal.valueOf(0.5));
        when(familyParametersScoreProperties.getMaritalStatusScoreRatio()).thenReturn(BigDecimal.valueOf(0.5));
        when(familyParametersScoreProperties.getHighNumberOfDependents()).thenReturn(3);
        when(familyParametersScoreProperties.getMediumNumberOfDependents()).thenReturn(2);
        when(familyParametersScoreProperties.getLowNumberOfDependents()).thenReturn(1);
        BigDecimal expectedResult = BigDecimal.valueOf(85);

        BigDecimal resultEvaluation = familyParametersEvaluator.evaluateFamilyParameters(scoringDataDtoWithNoDependentAndSingle);

        Assertions.assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateFamilyParametersTest_whenOneDependentAndSingle_thenReturnResult() {
        ReflectionTestUtils.setField(familyParametersEvaluator, "scoreAccuracy", 0);
        when(familyParametersScoreProperties.getLowNumberOfDependentsCoef()).thenReturn(BigDecimal.valueOf(0.7));
        when(familyParametersScoreProperties.getMarriedStatusCoef()).thenReturn(BigDecimal.valueOf(1));
        when(familyParametersScoreProperties.getNumberOfDependentScoreRatio()).thenReturn(BigDecimal.valueOf(0.5));
        when(familyParametersScoreProperties.getMaritalStatusScoreRatio()).thenReturn(BigDecimal.valueOf(0.5));
        when(familyParametersScoreProperties.getHighNumberOfDependents()).thenReturn(3);
        when(familyParametersScoreProperties.getMediumNumberOfDependents()).thenReturn(2);
        when(familyParametersScoreProperties.getLowNumberOfDependents()).thenReturn(1);
        BigDecimal expectedResult = BigDecimal.valueOf(85);

        BigDecimal resultEvaluation = familyParametersEvaluator.evaluateFamilyParameters(scoringDataDtoMarriedWithOneDependent);

        Assertions.assertEquals(expectedResult, resultEvaluation);
    }
}