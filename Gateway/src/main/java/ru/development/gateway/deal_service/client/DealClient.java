package ru.development.gateway.deal_service.client;

import ru.development.gateway.model.dto.FinishRegistrationRequestDto;

public interface DealClient {
    void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId);

    void sendDocuments(String statementId);

    void updateStatementDocStatus(String statementId);

    void signDocuments(String statementId);

    void processSesCode(String statementId, String code);
}
