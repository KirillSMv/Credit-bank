package ru.development.gateway.deal_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.gateway.error_handler.ErrorProcessingRequest;
import ru.development.gateway.error_handler.LoanRefusalException;
import ru.development.gateway.model.Statement;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealWebClientImpl implements DealClient {
    private final WebClient webClient;
    private final DealMSProperties dealMSProperties;

    @Value("${deal.serverUrl}")
    private String serverUrl;

    @Override
    public void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId) {
        webClient.post()
                .uri(dealMSProperties.getFinalizeLoanParametersUrl(), statementId)
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void sendDocuments(String statementId) {
        webClient.post()
                .uri(dealMSProperties.getSendDocumentsUrl(), statementId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void updateStatementDocStatus(String statementId) {
        webClient.post()
                .uri(dealMSProperties.getUpdateStatementDocStatusUrl(), statementId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void signDocuments(String statementId) {
        webClient.post()
                .uri(dealMSProperties.getSignDocumentsUrl(), statementId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void processSesCode(String statementId, String code) {
        webClient.post()
                .uri(serverUrl, uriBuilder -> uriBuilder
                        .path("deal/document/{statementId}/code")
                        .queryParam("code", code)
                        .build(statementId))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public List<Statement> getStatements(Integer offset, Integer size) {
        return webClient.get()
                .uri(serverUrl, uriBuilder -> uriBuilder
                        .path(dealMSProperties.getGetStatementsUrl())
                        .queryParam("offset", offset)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(new ParameterizedTypeReference<List<Statement>>() {
                })
                .block();
    }

    @Override
    public Statement getStatementById(String statementId) {
        return webClient.get()
                .uri(dealMSProperties.getGetStatementByIdUrl(), statementId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        this::processResponse)
                .bodyToMono(Statement.class)
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
