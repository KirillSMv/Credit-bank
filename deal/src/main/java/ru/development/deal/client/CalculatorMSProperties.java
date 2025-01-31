package ru.development.deal.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "calculator")
public class CalculatorMSProperties {
    private String calculatorServerUrl;
    private String calculateOffersUri;
    private String calculateCreditParametersUri;
}
