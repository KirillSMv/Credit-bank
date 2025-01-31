package ru.development.statement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.statement.client.StatementClient;
import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementClient statementClient;

    @Override
    public List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto) {
        List<LoanOfferDto> loanOfferDtos = statementClient.requestLoanOffers(dto);
        log.debug("statementIdUuid заявки = {}, loanOfferDtos size = {}", loanOfferDtos.get(0).getStatementIdUuid(), loanOfferDtos.size());
        return loanOfferDtos;
    }

    @Override
    public void selectOffer(LoanOfferDto dto) {
        statementClient.selectOffer(dto);
    }
}
