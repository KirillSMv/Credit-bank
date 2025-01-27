package ru.development.gateway.statement_service.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "statement")
@Getter
@Component
@Setter
public class StatementMSProperties {
    private String serverUrl;
    private String processLoanStatementUrl;
    private String selectLoanOfferUrl;
}
