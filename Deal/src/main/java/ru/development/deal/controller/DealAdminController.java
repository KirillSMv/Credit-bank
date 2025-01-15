package ru.development.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.development.deal.model.Statement;
import ru.development.deal.service.interfaces.DealAdminService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Tag(name = "Сделка", description = "методы для работы с МС 'Сделка' от имени администратора")
@Validated
public class DealAdminController {
    private final DealAdminService dealAdminService;

    @PutMapping("/statement/{statementId}/status")
    @Operation(summary = "запрос на обновления статуса заявки в связи с готовность документов")
    public void updateStatementDocStatus(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=PUT, URI=/deal/admin/statement/{statementId}/status, переменная пути {}", statementId);
        dealAdminService.updateStatementDocStatus(statementId);
    }

    @GetMapping("/statement/{statementId}")
    @Operation(summary = "запрос на получение заявки по id")
    public ResponseEntity<Statement> getStatementById(@PathVariable(name = "statementId") String statementId) {
        log.info("Получен запрос: метод=PUT, URI=/deal/admin/statement/{statementId}, переменная пути {}", statementId);
        return new ResponseEntity<>(dealAdminService.getStatementById(statementId), HttpStatus.OK);
    }

    @GetMapping("/statement")
    @Operation(summary = "запрос на получение постраничного списка заявок")
    public ResponseEntity<List<Statement>> getStatementById(
            @RequestParam(value = "offset", defaultValue = "0") @Min(value = 0) Integer offset,
            @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) @Max(value = 10) Integer size) {
        log.info("Получен запрос: метод=PUT, URI=/deal/admin/statement");
        return new ResponseEntity<>(dealAdminService.getStatements(PageRequest.of(offset, size)), HttpStatus.OK);
    }
}
