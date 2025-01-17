package ru.development.calculator.service.scoring;

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
import ru.development.calculator.service.scoring.score_properties.ScoringParametersRatioProperties;
import ru.development.calculator.service.scoring.services.interfaces.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class LoanOfferDtoScoringServiceFICOImplTest {

    @Mock
    private EmploymentScoringEvaluator employmentScoringEvaluator;
    @Mock
    private CreditHistoryScoringEvaluator creditHistoryScoringEvaluator;
    @Mock
    private ClientLoanBurdenEvaluator clientLoanBurdenEvaluator;
    @Mock
    private FamilyParametersEvaluator familyParametersEvaluator;
    @Mock
    private LoanAmountEvaluator loanAmountEvaluator;
    @Mock
    private AgeGenderEvaluator ageGenderEvaluator;
    @Mock
    private ScoringParametersRatioProperties scoringParametersRatioProperties;
    @InjectMocks
    private ScoringServiceFICOImpl scoringServiceFICO;
    private static EmploymentDto employmentDto;
    private static ScoringDataDto scoringDataDto;


    @BeforeAll
    static void setUp() {
        employmentDto = EmploymentDto.builder()
                .employerINN("1111111111")
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(60000))
                .positionType(PositionType.TOP_MANAGER)
                .workExperienceTotal(120)
                .workExperienceCurrent(120).build();

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
                .dependentAmount(3)
                .accountNumber(null)
                .maritalStatus(MaritalStatus.MARRIED)
                .isInsuranceEnabled(false)
                .isSalaryClient(false).build();
    }

    @Test
    void getScoreTest() {
        ReflectionTestUtils.setField(scoringServiceFICO, "scoreAccuracy", 0);
        when(employmentScoringEvaluator.evaluateEmploymentData(any(EmploymentDto.class))).thenReturn(BigDecimal.valueOf(94));
        when(creditHistoryScoringEvaluator.evaluateCreditHistory(any(ScoringDataDto.class))).thenReturn(BigDecimal.valueOf(50));
        when(clientLoanBurdenEvaluator.evaluateCurrentLoadBurden(any(ScoringDataDto.class))).thenReturn(BigDecimal.valueOf(50));
        when(familyParametersEvaluator.evaluateFamilyParameters(any(ScoringDataDto.class))).thenReturn(BigDecimal.valueOf(65));
        when(loanAmountEvaluator.evaluateAmountOfLoan(any(ScoringDataDto.class))).thenReturn(BigDecimal.valueOf(100));
        when(ageGenderEvaluator.evaluateGenderWithAge(any(Gender.class), any(LocalDate.class))).thenReturn(BigDecimal.valueOf(80));
        when(scoringParametersRatioProperties.getEmploymentScoreRatio()).thenReturn(BigDecimal.valueOf(0.17));
        when(scoringParametersRatioProperties.getCreditHistoryScoreRatio()).thenReturn(BigDecimal.valueOf(0.15));
        when(scoringParametersRatioProperties.getClientLoanBurdenScoreRatio()).thenReturn(BigDecimal.valueOf(0.2));
        when(scoringParametersRatioProperties.getFamilyParametersScoreRatio()).thenReturn(BigDecimal.valueOf(0.13));
        when(scoringParametersRatioProperties.getLoanAmountScoreRatio()).thenReturn(BigDecimal.valueOf(0.2));
        when(scoringParametersRatioProperties.getAgeGenderScoreRatio()).thenReturn(BigDecimal.valueOf(0.15));
        BigDecimal expectedScore = BigDecimal.valueOf(707);

        BigDecimal resultScore = scoringServiceFICO.getScore(scoringDataDto);

        Assertions.assertEquals(expectedScore, resultScore);
    }
}