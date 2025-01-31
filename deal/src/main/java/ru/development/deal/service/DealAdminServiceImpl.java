package ru.development.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.development.deal.error_handler.NoObjectFoundException;
import ru.development.deal.model.Statement;
import ru.development.deal.model.StatusHistory;
import ru.development.deal.model.enums.ApplicationStatus;
import ru.development.deal.model.enums.ChangeType;
import ru.development.deal.repository.StatementRepository;
import ru.development.deal.service.interfaces.DealAdminService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealAdminServiceImpl implements DealAdminService {
    private final StatementRepository statementRepository;


    @Override
    @Transactional
    public void updateStatementDocStatus(String statementId) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn("Заявки с таким id не найдено, id = {}", statementId);
                    return new NoObjectFoundException("Заявки на кредит с такими данными не найдено");
                }
        );
        statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        updateStatusHistory(statement, ApplicationStatus.DOCUMENT_CREATED);
        log.debug("обновлена заявка с id {}, новый статус - {}", statement.getStatementIdUuid(), statement.getStatus());
        statementRepository.save(statement);
    }

    @Override
    public Statement getStatementById(String statementId) {
        return statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn("Заявки с таким id не найдено, id = {}", statementId);
                    return new NoObjectFoundException("Заявки на кредит с такими данными не найдено");
                }
        );
    }

    @Override
    public List<Statement> getStatements(Pageable pageable) {
        List<Statement> statements = statementRepository.findAll(pageable).getContent();
        log.debug("statements size = {}", statements.size());
        return statements;
    }

    private void updateStatusHistory(Statement statement, ApplicationStatus applicationStatus) {
        List<StatusHistory> statusHistory = statement.getStatusHistory();
        statusHistory.add(StatusHistory.builder()
                .status(applicationStatus)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
        statement.setStatusHistory(statusHistory);
    }
}
