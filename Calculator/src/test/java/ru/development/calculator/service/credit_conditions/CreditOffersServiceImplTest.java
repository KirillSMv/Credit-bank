package ru.development.calculator.service.credit_conditions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestDto;
import ru.development.calculator.service.prescoring.PreScoringService;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.AnnuityLoanMonthlyPaymentCalculator;
import ru.development.calculator.service.support.interfaces.InsuranceManager;
import ru.development.calculator.service.support.interfaces.SalaryClientsManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditOffersServiceImplTest {
    @Mock
    private CreditProperties creditProperties;
    @Mock
    private PreScoringService preScoringService;
    @Mock
    private InsuranceManager insuranceManager;
    @Mock
    private SalaryClientsManager salaryClientsManager;
    @Mock
    private AnnuityLoanMonthlyPaymentCalculator annuityLoanMonthlyPaymentCalculator;
    @InjectMocks
    private CreditOffersServiceImpl creditConditionsService;
    private static LoanStatementRequestDto loanStatementRequestDto;

    @BeforeAll
    static void get() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void calculateCreditConditionsTest() {
        when(creditProperties.getCentralBankInterestRate()).thenReturn(BigDecimal.valueOf(21));
        when(creditProperties.getAverageRateAboveCentralBInterestRate()).thenReturn(BigDecimal.valueOf(6));
        when(salaryClientsManager.getRateForSalaryClient(BigDecimal.valueOf(27))).thenReturn(BigDecimal.valueOf(26));
        when(creditProperties.getFinalCalcAccuracy()).thenReturn(2);
        when(insuranceManager.getAmountWithInsurance(BigDecimal.valueOf(500000))).thenReturn(BigDecimal.valueOf(510000.00));
        when(insuranceManager.getRateWithInsurance(BigDecimal.valueOf(27))).thenReturn(BigDecimal.valueOf(26));
        when(insuranceManager.getRateWithInsurance(BigDecimal.valueOf(26))).thenReturn(BigDecimal.valueOf(25));
        when(annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(BigDecimal.valueOf(27), BigDecimal.valueOf(500000),
                12)).thenReturn(BigDecimal.valueOf(48008.70));
        when(annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(BigDecimal.valueOf(26), BigDecimal.valueOf(500000),
                12)).thenReturn(BigDecimal.valueOf(47765.07));
        when(annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(BigDecimal.valueOf(26), BigDecimal.valueOf(510000.00),
                12)).thenReturn(BigDecimal.valueOf(48720.37));
        when(annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(BigDecimal.valueOf(25), BigDecimal.valueOf(510000.00),
                12)).thenReturn(BigDecimal.valueOf(48472.54));


        List<LoanOfferDto> expectedList = new ArrayList<>();
        expectedList.add(new LoanOfferDto(BigDecimal.valueOf(500000),
                BigDecimal.valueOf(576104.40).setScale(2, RoundingMode.HALF_UP), 12, BigDecimal.valueOf(48008.70).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(27),
                false, false));
        expectedList.add(new LoanOfferDto(BigDecimal.valueOf(500000),
                BigDecimal.valueOf(573180.84), 12, BigDecimal.valueOf(47765.07), BigDecimal.valueOf(26),
                false, true));
        expectedList.add(new LoanOfferDto(BigDecimal.valueOf(500000),
                BigDecimal.valueOf(584644.44), 12, BigDecimal.valueOf(48720.37), BigDecimal.valueOf(26),
                true, false));
        expectedList.add(new LoanOfferDto(BigDecimal.valueOf(500000),
                BigDecimal.valueOf(581670.48), 12, BigDecimal.valueOf(48472.54), BigDecimal.valueOf(25),
                true, true));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();
        List<LoanOfferDto> resultList = creditConditionsService.calculateCreditOffers(loanStatementRequestDto);

        Assertions.assertEquals(expectedList, resultList);
    }
}