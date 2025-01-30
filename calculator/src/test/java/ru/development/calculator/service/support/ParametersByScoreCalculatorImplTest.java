package ru.development.calculator.service.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.CreditParametersShort;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.service.properties.CreditProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParametersByScoreCalculatorImplTest {

    @Mock
    private CreditProperties creditProperties;
    @InjectMocks
    private ParametersByScoreCalculatorImpl parametersByScoreCalculator;

    private static EmploymentDto employmentDto;
    private static ScoringDataDto scoringDataDto;


    @BeforeAll
    static void setUp() {
        employmentDto = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.OTHER)
                .workExperienceTotal(300)
                .workExperienceCurrent(50).build();

        scoringDataDto = ScoringDataDto.builder()
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
    }

    @Test
    void getRateAndAmountTest_whenScoreIsBelowClassTwo_thenThrowScoringException() {
        when(creditProperties.getFirstRatingClass()).thenReturn(300);
        when(creditProperties.getSecondRatingClass()).thenReturn(500);
        BigDecimal value = BigDecimal.valueOf(400);
        Assertions.assertThrows(ScoringException.class, () -> parametersByScoreCalculator.getRateAndAmount(value, scoringDataDto));
    }

    @Test
    void getRateAndAmountTest_whenScoreBetweenSecondToThirdClass_thenReturnResult() {
        when(creditProperties.getFirstRatingClass()).thenReturn(300);
        when(creditProperties.getSecondRatingClass()).thenReturn(500);
        when(creditProperties.getThirdRatingClass()).thenReturn(600);
        when(creditProperties.getLoanAmountSecondToThirdClass()).thenReturn(BigDecimal.valueOf(10));
        when(creditProperties.getCentralBankInterestRate()).thenReturn(BigDecimal.valueOf(21));
        when(creditProperties.getAverageRateAboveCentralBInterestRate()).thenReturn(BigDecimal.valueOf(6));
        when(creditProperties.getRateDifferenceSecondToThirdClass()).thenReturn(BigDecimal.valueOf(2));
        CreditParametersShort expectedCreditParametersShort = new CreditParametersShort(BigDecimal.valueOf(500000), BigDecimal.valueOf(29), null);

        CreditParametersShort resultEvaluation = parametersByScoreCalculator.getRateAndAmount(BigDecimal.valueOf(550), scoringDataDto);
        Assertions.assertEquals(expectedCreditParametersShort, resultEvaluation);
    }

    @Test
    void getRateAndAmountTest_whenScoreBetweenThirdToForthClass_thenReturnResult() {
        when(creditProperties.getFirstRatingClass()).thenReturn(300);
        when(creditProperties.getSecondRatingClass()).thenReturn(500);
        when(creditProperties.getThirdRatingClass()).thenReturn(600);
        when(creditProperties.getForthRatingClass()).thenReturn(690);
        when(creditProperties.getLoanAmountThirdToForthClass()).thenReturn(BigDecimal.valueOf(16));
        when(creditProperties.getCentralBankInterestRate()).thenReturn(BigDecimal.valueOf(21));
        when(creditProperties.getAverageRateAboveCentralBInterestRate()).thenReturn(BigDecimal.valueOf(6));
        when(creditProperties.getRateDifferenceThirdToForthClass()).thenReturn(BigDecimal.valueOf(0));
        CreditParametersShort expectedCreditParametersShort = new CreditParametersShort(BigDecimal.valueOf(500000), BigDecimal.valueOf(27), null);

        CreditParametersShort resultEvaluation = parametersByScoreCalculator.getRateAndAmount(BigDecimal.valueOf(650), scoringDataDto);
        Assertions.assertEquals(expectedCreditParametersShort, resultEvaluation);
    }

    @Test
    void getRateAndAmountTest_whenScoreBetweenForthToFifthClass_thenReturnResult() {
        when(creditProperties.getFirstRatingClass()).thenReturn(300);
        when(creditProperties.getSecondRatingClass()).thenReturn(500);
        when(creditProperties.getThirdRatingClass()).thenReturn(600);
        when(creditProperties.getForthRatingClass()).thenReturn(690);
        when(creditProperties.getFifthRatingClass()).thenReturn(850);
        when(creditProperties.getLoanAmountForthToFifthClass()).thenReturn(BigDecimal.valueOf(25));
        when(creditProperties.getCentralBankInterestRate()).thenReturn(BigDecimal.valueOf(21));
        when(creditProperties.getAverageRateAboveCentralBInterestRate()).thenReturn(BigDecimal.valueOf(6));
        when(creditProperties.getRateDifferenceForthToFifthClass()).thenReturn(BigDecimal.valueOf(2));
        CreditParametersShort expectedCreditParametersShort = new CreditParametersShort(BigDecimal.valueOf(500000), BigDecimal.valueOf(25), null);

        CreditParametersShort resultEvaluation = parametersByScoreCalculator.getRateAndAmount(BigDecimal.valueOf(750), scoringDataDto);
        Assertions.assertEquals(expectedCreditParametersShort, resultEvaluation);
    }
}