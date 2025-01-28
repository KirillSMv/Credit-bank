package ru.development.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.development.deal.model.dto.FinishRegistrationRequestDto;
import ru.development.deal.model.dto.LoanOfferDto;
import ru.development.deal.model.dto.LoanStatementRequestDto;
import ru.development.deal.service.interfaces.DealService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name = "Сделка", description = "методы для работы с МС 'Сделка'")
public class DealController {
    private final DealService dealService;

    @PostMapping("/statement")
    @Operation(summary = "обработка заявки на кредит")
    public ResponseEntity<List<LoanOfferDto>> processLoanStatement(@Parameter(description = "заяка на кредит") @RequestBody LoanStatementRequestDto dto) {
        log.info("Получен запрос: метод=POST, URI=/deal/statement");
        return new ResponseEntity<>(dealService.processLoanStatement(dto), HttpStatus.OK);
    }

    @PostMapping("/offer/select")
    @Operation(summary = "сохранение выбранного предложения по кредиту")
    public void selectOffer(@Parameter(description = "предложение по кредиту") @RequestBody LoanOfferDto dto) {
        log.info("Получен запрос: метод=POST, URI=/deal/offer/select");
        dealService.selectOffer(dto);
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(summary = "расчет и сохранение параметров кредита")
    public void finalizeLoanParameters(@Parameter(description = "данные для завершения обработки заявка на кредит")
                                       @RequestBody FinishRegistrationRequestDto dto,
                                       @PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/deal/calculate/{statementId}, переменная пути {}", statementId);
        dealService.finalizeLoanParameters(dto, statementId);
    }

    @PostMapping("/document/{statementId}/send")
    @Operation(summary = "запрос на отправку документов")
    public void sendDocuments(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/deal/document/{statementId}/send, переменная пути {}", statementId);
        dealService.sendDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/sign")
    @Operation(summary = "запрос на подписание документов")
    public void signDocuments(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/deal/document/{statementId}/sign, переменная пути {}", statementId);
        dealService.signDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/code")
    @Operation(summary = "обработка кода от клиента для выдачи кредита")
    public void processSesCode(@PathVariable(name = "statementId") String statementId, @RequestParam("code") String code) {
        log.info("Получен запрос: метод=POST, URI=/deal/document/{statementId}/code, переменная пути {}", statementId);
        dealService.processSesCode(statementId, code);
    }


}
