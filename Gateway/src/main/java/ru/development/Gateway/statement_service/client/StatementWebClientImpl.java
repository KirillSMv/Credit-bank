package ru.development.Gateway.statement_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.Gateway.error_handler.ErrorProcessingRequest;
import ru.development.Gateway.error_handler.LoanRefusalException;
import ru.development.Gateway.statement_service.StatementMSProperties;
import ru.development.Gateway.statement_service.model.LoanOfferDto;
import ru.development.Gateway.statement_service.model.LoanStatementRequestDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatementWebClientImpl implements StatementClient {
    private final WebClient webClient;
    private final StatementMSProperties statementMSProperties;

    @Override
    public List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto) {
        return webClient.post()
                .uri(statementMSProperties.getStatementServerUrl() + statementMSProperties.getProcessLoanStatementUrl())
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(new ParameterizedTypeReference<List<LoanOfferDto>>() {
                })
                .block();
    }

    @Override
    public void selectOffer(LoanOfferDto dto) {
        webClient.post()
                .uri(statementMSProperties.getStatementServerUrl() + statementMSProperties.getSelectLoanOfferUrl())
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    private Mono<? extends Throwable> processResponse(ClientResponse clientResponse) {
        int statusCode = clientResponse.statusCode().value();
        return switch (statusCode) {
            case 400 -> clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        log.debug("Выброшено исключение сервером - {}", errorBody);
                        return Mono.error(new ErrorProcessingRequest(errorBody));
                    });
            case 409 -> clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        log.debug("Выброшено исключение сервером - {}", errorBody);
                        return Mono.error(new LoanRefusalException(errorBody));
                    });
            default -> clientResponse.createException();
        };
    }
}
