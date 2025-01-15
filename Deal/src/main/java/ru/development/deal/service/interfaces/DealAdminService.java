package ru.development.deal.service.interfaces;

import org.springframework.data.domain.Pageable;
import ru.development.deal.model.Statement;

import java.util.List;

public interface DealAdminService {
    void updateStatementDocStatus(String statementId);

    Statement getStatementById(String statementId);

    List<Statement> getStatements(Pageable pageable);
}
