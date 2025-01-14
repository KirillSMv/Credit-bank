package ru.development.Gateway.statement_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.development.Gateway.statement_service.client.StatementClient;
import ru.development.Gateway.statement_service.model.LoanOfferDto;
import ru.development.Gateway.statement_service.model.LoanStatementRequestDto;

import java.util.List;

@RestController
@RequestMapping("/statement")
@Slf4j
@RequiredArgsConstructor
public class StatementController {

    private final StatementClient statementClient;

    @PostMapping
    @Operation(summary = "обработка заявки на кредит")
    public ResponseEntity<List<LoanOfferDto>> processLoanStatement(@Parameter(description = "заяка на кредит") @RequestBody @Valid LoanStatementRequestDto dto) {
        log.info("Получен запрос: метод=POST, URI=/statement");
        return new ResponseEntity<>(statementClient.processLoanStatement(dto), HttpStatus.OK);
    }

    @PostMapping("/offer")
    @Operation(summary = "фиксация выбранного предложения по кредиту")
    public void selectOffer(@Parameter(description = "предложение по кредиту") @RequestBody LoanOfferDto dto) {
        log.info("Получен запрос: метод=POST, URI=/statement/offer");
        statementClient.selectOffer(dto);
    }
}
