package ru.development.Deal.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
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
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(LoanRefusalException::new))
                .onStatus(
                        HttpStatus.FORBIDDEN::equals,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        return Mono.error(new LoanRefusalException(errorBody));
                                    });
                        }
                )
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
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(LoanRefusalException::new))
                .onStatus(
                        HttpStatus.FORBIDDEN::equals,
                        clientResponse -> {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        return Mono.error(new LoanRefusalException(errorBody));
                                    });
                        }
                )
                .bodyToMono(CreditDto.class)
                .block();
    }
}
