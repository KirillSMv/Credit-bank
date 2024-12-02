package ru.development.calculator.service.credit_conditions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;
import ru.development.calculator.service.prescoring.PreScoringService;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.AnnuityLoanMonthlyPaymentCalculator;
import ru.development.calculator.service.support.interfaces.InsuranceManager;
import ru.development.calculator.service.support.interfaces.SalaryClientsManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditOffersServiceImpl implements CreditOffersService {
    private final CreditProperties creditProperties;
    private final PreScoringService preScoringService;
    private final InsuranceManager insuranceManager;
    private final SalaryClientsManager salaryClientsManager;
    private final AnnuityLoanMonthlyPaymentCalculator annuityLoanMonthlyPaymentCalculator;

    @Override
    public List<LoanOfferDto> calculateCreditOffers(LoanStatementRequestFullDto loanStatementRequestFullDto) {
        preScoringService.preScoring(loanStatementRequestFullDto);
        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(getCreditOffer(true, true, loanStatementRequestFullDto));
        offers.add(getCreditOffer(true, false, loanStatementRequestFullDto));
        offers.add(getCreditOffer(false, true, loanStatementRequestFullDto));
        offers.add(getCreditOffer(false, false, loanStatementRequestFullDto));
        return offers.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();
    }

    private LoanOfferDto getCreditOffer(boolean IsSalaryClient, boolean isInsuranceEnabled, LoanStatementRequestFullDto dto) {
        BigDecimal rate = getNormalRateForLoan();
        BigDecimal amount = dto.getAmount();
        int term = dto.getTerm();

        if (IsSalaryClient) {
            rate = salaryClientsManager.getRateForSalaryClient(rate);
        }
        if (isInsuranceEnabled) {
            rate = insuranceManager.getRateWithInsurance(rate);
            amount = insuranceManager.getAmountWithInsurance(amount);
        }
        log.debug("метод getCreditOffer(), IsSalaryClient ={}, isInsuranceEnabled = {}, rate = {}, amount = {}", IsSalaryClient, isInsuranceEnabled, rate, amount);
        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(rate, amount, term)
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        log.debug("метод getCreditOffer(), rate = {}, totalAmount: {}", rate, totalAmount);
        return new LoanOfferDto(dto.getStatementId(), dto.getAmount(), totalAmount, term, monthlyPayment, rate, isInsuranceEnabled, IsSalaryClient);
    }

    private BigDecimal getNormalRateForLoan() {
        return creditProperties.getCentralBankInterestRate().add(creditProperties.getAverageRateAboveCentralBInterestRate());
    }
}
