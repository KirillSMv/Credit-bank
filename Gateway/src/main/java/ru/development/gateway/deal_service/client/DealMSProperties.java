package ru.development.gateway.deal_service.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "deal")
@Getter
@Component
@Setter
public class DealMSProperties {
    private String finalizeLoanParametersUrl;
    private String sendDocumentsUrl;
    private String updateStatementDocStatusUrl;
    private String signDocumentsUrl;
    private String processSesCodeUrl;
    private String getStatementByIdUrl;
    private String getStatementsUrl;


}
