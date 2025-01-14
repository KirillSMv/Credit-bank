package ru.development.gateway.deal_service.client;

import ru.development.gateway.model.Statement;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;

import java.util.List;

public interface DealClient {
    void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId);

    void sendDocuments(String statementId);

    void updateStatementDocStatus(String statementId);

    void signDocuments(String statementId);

    void processSesCode(String statementId, String code);

    List<Statement> getStatements(Integer offset, Integer size);

    Statement getStatementById(String statementId);
}
