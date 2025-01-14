package ru.development.gateway.deal_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.development.gateway.deal_service.client.DealClient;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;

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
}
