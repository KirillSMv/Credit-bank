package ru.development.Gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    @Bean
    public WebClient getClient(WebClient.Builder builder) {
        return builder
                .defaultHeaders(this::addDefaultHeaders)
                .build();
    }

    private void addDefaultHeaders(final HttpHeaders headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
    }
}
