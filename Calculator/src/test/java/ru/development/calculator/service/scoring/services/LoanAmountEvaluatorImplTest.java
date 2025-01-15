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
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.service.scoring.score_properties.LoanAmountParametersProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanAmountEvaluatorImplTest {

    @Mock
    private LoanAmountParametersProperties loanAmountParametersProperties;

    @InjectMocks
    private LoanAmountEvaluatorImpl loanAmountEvaluator;
    private static EmploymentDto employmentDto;
    private static ScoringDataDto scoringDataDtoWithBigAmountRequest;
    private static ScoringDataDto scoringDataDtoWithLowAmountRequest;
    private static ScoringDataDto scoringDataDtoWithAverageAmountRequest;

    @BeforeAll
    static void setUp() {
        employmentDto = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        scoringDataDtoWithBigAmountRequest = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(1500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(employmentDto)
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

        scoringDataDtoWithLowAmountRequest = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(employmentDto)
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

        scoringDataDtoWithAverageAmountRequest = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(750000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(employmentDto)
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

    }

    @Test
    void evaluateAmountOfLoanTest_whenLoanAmountIsTooBig_thenScoringExceptionThrown() {
        ReflectionTestUtils.setField(loanAmountEvaluator, "scoreAccuracy", 0);
        when(loanAmountParametersProperties.getMaxLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(24));
        assertThrows(ScoringException.class, () -> loanAmountEvaluator.evaluateAmountOfLoan(scoringDataDtoWithBigAmountRequest));
    }

    @Test
    void evaluateAmountOfLoanTest_whenLoanAmountRequestIsLow_thenResultIsReturned() {
        ReflectionTestUtils.setField(loanAmountEvaluator, "scoreAccuracy", 0);
        when(loanAmountParametersProperties.getMaxLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(24));
        when(loanAmountParametersProperties.getLowLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(12));
        when(loanAmountParametersProperties.getLowLoanToSalariesRatioCoef()).thenReturn(BigDecimal.valueOf(1));
        BigDecimal expectedResult = BigDecimal.valueOf(100);

        BigDecimal resultEvaluation = loanAmountEvaluator.evaluateAmountOfLoan(scoringDataDtoWithLowAmountRequest);
        assertEquals(expectedResult, resultEvaluation);
    }

    @Test
    void evaluateAmountOfLoanTest_whenLoanAmountRequestIsAverage_thenResultIsReturned() {
        ReflectionTestUtils.setField(loanAmountEvaluator, "scoreAccuracy", 0);
        when(loanAmountParametersProperties.getMaxLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(24));
        when(loanAmountParametersProperties.getLowLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(12));
        when(loanAmountParametersProperties.getAverageLoanToSalariesRatio()).thenReturn(BigDecimal.valueOf(16));
        when(loanAmountParametersProperties.getAverageLoanToSalariesRatioCoef()).thenReturn(BigDecimal.valueOf(0.8));
        BigDecimal expectedResult = BigDecimal.valueOf(80);

        BigDecimal resultEvaluation = loanAmountEvaluator.evaluateAmountOfLoan(scoringDataDtoWithAverageAmountRequest);
        assertEquals(expectedResult, resultEvaluation);
    }
}