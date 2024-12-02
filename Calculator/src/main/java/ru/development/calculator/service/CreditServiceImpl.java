package ru.development.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.CreditParameters;
import ru.development.calculator.model.CreditParametersShort;
import ru.development.calculator.model.dto.*;
import ru.development.calculator.model.dto.mapper.CreditDtoMapper;
import ru.development.calculator.service.credit_conditions.CreditOffersService;
import ru.development.calculator.service.credit_parameters.AnnuityLoanParametersService;
import ru.development.calculator.service.scoring.ScoringService;
import ru.development.calculator.service.support.interfaces.InsuranceManager;
import ru.development.calculator.service.support.interfaces.LoanFeasibilityAssessor;
import ru.development.calculator.service.support.interfaces.ParametersByScoreCalculator;
import ru.development.calculator.service.support.interfaces.SalaryClientsManager;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditOffersService creditOffersService;
    private final AnnuityLoanParametersService annuityLoanParametersService;
    private final ScoringService scoringService;
    private final ParametersByScoreCalculator parametersByScoreCalculator;
    private final SalaryClientsManager salaryClientsManager;
    private final InsuranceManager insuranceManager;
    private final CreditDtoMapper creditDtoMapper;
    private final LoanFeasibilityAssessor loanFeasibilityAssessor;

    @Override
    public List<LoanOfferDto> calculateCreditConditions(LoanStatementRequestFullDto loanStatementRequestFullDto) {
        return creditOffersService.calculateCreditOffers(loanStatementRequestFullDto);
    }

    @Override
    public CreditDto calculateCreditParameters(ScoringDataDto scoringDataDto) {
        BigDecimal score = scoringService.getScore(scoringDataDto);
        CreditParametersShort creditParametersShort = parametersByScoreCalculator.getRateAndAmount(score, scoringDataDto);
        creditParametersShort.setTerm(scoringDataDto.getTerm());
        if (scoringDataDto.getIsInsuranceEnabled()) {
            creditParametersShort.setRate(insuranceManager.getRateWithInsurance(creditParametersShort.getRate()));
            creditParametersShort.setAmount(insuranceManager.getAmountWithInsurance(creditParametersShort.getAmount()));
        }
        if (scoringDataDto.getIsSalaryClient()) {
            creditParametersShort.setRate(salaryClientsManager.getRateForSalaryClient(creditParametersShort.getRate()));
        }
        CreditParameters creditParameters = annuityLoanParametersService.calculateAnnuityLoanParameters(creditParametersShort);
        loanFeasibilityAssessor.assessIfLoanFeasible(creditParameters.getRate(), creditParameters.getAmount(),
                scoringDataDto.getEmployment().getSalary(), creditParameters.getTerm());
        return creditDtoMapper.toCreditDto(creditParameters, new ExtraCreditParameters(scoringDataDto.getIsInsuranceEnabled(),
                scoringDataDto.getIsSalaryClient()));
    }
}
