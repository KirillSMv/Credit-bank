package ru.development.Dossier.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.Dossier.client.interfaces.ClientService;
import ru.development.Dossier.error_handler.ErrorProcessingRequest;
import ru.development.Dossier.error_handler.LoanRefusalException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final WebClient webClient;
    private final DealMSProperties dealMSProperties;

    @Override
    public void send(String statementId) {
        log.debug(String.format("Отправляется запрос на url: %s", dealMSProperties.getDealServerUrl() + dealMSProperties.getUpdateLoanStatementUrl()));
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
