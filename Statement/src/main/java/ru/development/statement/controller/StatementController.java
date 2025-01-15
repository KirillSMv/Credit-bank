package ru.development.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;
import ru.development.statement.service.StatementService;

import java.util.List;

@RestController
@RequestMapping("/statement")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Заявка", description = "методы для работы с МС 'Заявка'")
public class StatementController {
    private final StatementService service;

    @PostMapping
    @Operation(summary = "обработка заявки на кредит")
    public ResponseEntity<List<LoanOfferDto>> processLoanStatement(@Parameter(description = "заяка на кредит") @RequestBody @Valid LoanStatementRequestDto dto) {
        log.info("Получен запрос: метод=POST, URI=/statement");
        return new ResponseEntity<>(service.processLoanStatement(dto), HttpStatus.OK);
    }

    @PostMapping("/offer")
    @Operation(summary = "фиксация выбранного предложения по кредиту")
    public void selectOffer(@Parameter(description = "предложение по кредиту") @RequestBody LoanOfferDto dto) {
        log.info("Получен запрос: метод=POST, URI=/statement/offer");
        service.selectOffer(dto);
    }

}
