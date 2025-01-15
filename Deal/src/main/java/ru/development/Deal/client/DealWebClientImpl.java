package ru.development.Deal.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.Deal.client.interfaces.DealClient;
import ru.development.Deal.error_handler.ErrorProcessingRequest;
import ru.development.Deal.error_handler.LoanRefusalException;
import ru.development.Deal.model.dto.CreditDto;
import ru.development.Deal.model.dto.LoanOfferDto;
import ru.development.Deal.model.dto.LoanStatementRequestDto;
import ru.development.Deal.model.dto.ScoringDataDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealWebClientImpl implements DealClient {
    private final WebClient webClient;
    private final CalculatorMSProperties calculatorMSProperties;

    @Override
    public List<LoanOfferDto> calculateOffers(LoanStatementRequestDto dto) {
        log.debug("метод calculateOffers");
        List<LoanOfferDto> resultList = new ArrayList<>();
        resultList = webClient.post()
                .uri(calculatorMSProperties.getCalculateOffersUri())
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .onStatus(HttpStatusCode::isError, DealWebClientImpl::processResponse)
                .bodyToMono(new ParameterizedTypeReference<List<LoanOfferDto>>() {
                })
                .block();
        return resultList;
    }

    @Override
    public CreditDto calculateCreditParameters(ScoringDataDto dto) {
        log.debug("метод calculateCreditParameters");
        return webClient.post()
                .uri(calculatorMSProperties.getCalculateCreditParametersUri())
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .onStatus(HttpStatusCode::isError, DealWebClientImpl::processResponse)
                .bodyToMono(CreditDto.class)
                .block();
    }

    private static Mono<? extends Throwable> processResponse(ClientResponse clientResponse) {
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
