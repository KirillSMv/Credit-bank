package ru.development.dossier.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "deal")
@Getter
@Component
@Setter
public class DealMSProperties {
    private String dealServerUrl;
    private String updateLoanStatementUrl;
}


