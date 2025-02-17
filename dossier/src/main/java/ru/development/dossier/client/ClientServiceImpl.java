package ru.development.dossier.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.dossier.client.interfaces.ClientService;
import ru.development.dossier.error_handler.ErrorProcessingRequestException;
import ru.development.dossier.error_handler.LoanRefusalException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final WebClient webClient;
    private final DealMSProperties dealMSProperties;

    @Override
    public void send(String statementId) {
        log.debug("Отправляется запрос на url: {}{}", dealMSProperties.getDealServerUrl(), dealMSProperties.getUpdateLoanStatementUrl());
        webClient.put()
                .uri(dealMSProperties.getUpdateLoanStatementUrl(), statementId)
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
                        return Mono.error(new ErrorProcessingRequestException(errorBody));
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
