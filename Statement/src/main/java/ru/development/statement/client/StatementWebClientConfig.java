package ru.development.statement.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class StatementWebClientConfig {
    private final DealMSProperties dealMSProperties;

    @Bean
    public WebClient getClient(WebClient.Builder builder) {
        return builder
                .baseUrl(dealMSProperties.getDealServerUrl())
                .defaultHeaders(this::addDefaultHeaders)
                .build();
    }

    private void addDefaultHeaders(final HttpHeaders headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
    }
}
