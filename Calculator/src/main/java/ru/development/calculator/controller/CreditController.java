package ru.development.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.development.calculator.model.dto.CreditDto;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestDto;
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
    public ResponseEntity<List<LoanOfferDto>> calculateCreditConditions(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Получен запрос: метод=POST, URI=/calculator/offers");
        return new ResponseEntity<>(creditService.calculateCreditConditions(loanStatementRequestDto), HttpStatus.OK);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCreditParameters(@RequestBody @Valid ScoringDataDto scoringDataDto) {
        log.info("Получен запрос: метод=POST, URI=/calculator/calc");
        return new ResponseEntity<>(creditService.calculateCreditParameters(scoringDataDto), HttpStatus.OK);
    }
}
