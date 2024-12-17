package ru.development.statement.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.statement.exceptions.ErrorProcessingRequest;
import ru.development.statement.exceptions.LoanRefusalException;
import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatementWebClientImpl implements StatementClient {
    private final DealMSProperties dealMSProperties;
    private final WebClient webClient;

    @Override
    public List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto dto) {
        return webClient.post()
                .uri(dealMSProperties.getProcessLoanStatementUrl())
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
                .uri(dealMSProperties.getSelectLoanOfferUrl())
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
