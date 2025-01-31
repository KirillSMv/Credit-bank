package ru.development.gateway.deal_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.development.gateway.deal_service.client.DealClient;
import ru.development.gateway.model.Statement;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Сделка", description = "методы для работы с МС 'Сделка'")
public class DealController {
    private final DealClient dealClient;

    @PostMapping("/statement/registration/{statementId}")
    @Operation(summary = "расчет и сохранение параметров кредита")
    public void finalizeLoanParameters(@Parameter(description = "данные для завершения обработки заявка на кредит")
                                       @RequestBody @Valid FinishRegistrationRequestDto dto,
                                       @PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/statement/registration/{statementId}, переменная пути {}", statementId);
        dealClient.finalizeLoanParameters(dto, statementId);
    }

    @PostMapping("/document/{statementId}")
    @Operation(summary = "запрос на отправку документов")
    public void sendDocuments(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/document/{statementId}, переменная пути {}", statementId);
        dealClient.sendDocuments(statementId);

    }

    @PostMapping("/document/{statementId}/sign")
    @Operation(summary = "запрос на подписание документов")
    public void signDocuments(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/document/{statementId}/sign, переменная пути {}", statementId);
        dealClient.signDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/sign/code")
    @Operation(summary = "обработка кода от клиента для выдачи кредита")
    public void processSesCode(@PathVariable(name = "statementId") String statementId, @RequestParam("code") String code) {
        log.info("Получен запрос: метод=POST, URI=/document/{statementId}/sign/code, переменная пути {}", statementId);
        dealClient.processSesCode(statementId, code);
    }


    @GetMapping("/admin/statement/{statementId}")
    @Operation(summary = "запрос на получение заявки по id")
    public ResponseEntity<Statement> getStatementById(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=POST, URI=/admin/statement/{statementId}, переменная пути {}", statementId);
        return new ResponseEntity<>(dealClient.getStatementById(statementId), HttpStatus.OK);
    }

    @GetMapping("/admin/statement")
    @Operation(summary = "запрос на получение постраничного списка заявок")
    public ResponseEntity<List<Statement>> getStatements(
            @RequestParam(value = "offset", defaultValue = "0") @Min(value = 0) Integer offset,
            @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) @Max(value = 10) Integer size) {
        log.info("Получен запрос: метод=POST, URI=/admin/statement, offset = {}, size = {}", offset, size);
        return new ResponseEntity<>(dealClient.getStatements(offset, size), HttpStatus.OK);
    }
}

