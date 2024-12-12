package ru.development.Deal.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class DealWebClientConfig {
    private final CalculatorMSProperties calculatorMSProperties;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(calculatorMSProperties.getCalculatorServerUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
