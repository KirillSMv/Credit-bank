package ru.development.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.development.calculator.model.dto.CreditDto;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.CreditService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/calculator")
public class CreditController {
    private final CreditService creditService;

    @PostMapping("/offers")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<LoanOfferDto>> calculateCreditConditions(@RequestBody @Valid LoanStatementRequestFullDto loanStatementRequestFullDto) {
        log.debug("метод calculateCreditConditions, statementId = {}", loanStatementRequestFullDto.getStatementId());
        return new ResponseEntity<>(creditService.calculateCreditConditions(loanStatementRequestFullDto), HttpStatus.OK);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCreditParameters(@RequestBody @Valid ScoringDataDto scoringDataDto) {
        log.debug("метод calculateCreditParameters");
        return new ResponseEntity<>(creditService.calculateCreditParameters(scoringDataDto), HttpStatus.OK);
    }
}
