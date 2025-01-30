package ru.development.dossier.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.dossier.error_handler.ErrorProcessingRequestException;
import ru.development.dossier.error_handler.LoanRefusalException;

import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.client.WebClient.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    WebClient webClient;
    @Mock
    DealMSProperties dealMSProperties;
    @Mock
    RequestBodyUriSpec requestBodyUriSpec;

    @InjectMocks
    ClientServiceImpl clientService;

    @Mock
    RequestBodySpec requestHeadersSpec;

    @Mock
    ResponseSpec responseSpec;

    @Test
    void sendTest() {
        String dealServiceUrl = "deal:9090";
        String updateLoanStatementUrl = "/deal/admin/statement/{statementId}/status";
        String statementId = "63e8f05d-6d53-49b0-a4f5-80939b31a0d8";

        when(dealMSProperties.getDealServerUrl()).thenReturn(dealServiceUrl);
        when(dealMSProperties.getUpdateLoanStatementUrl()).thenReturn(updateLoanStatementUrl);

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(updateLoanStatementUrl, statementId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        clientService.send(statementId);

        Mockito.verify(dealMSProperties, times(2)).getUpdateLoanStatementUrl();
        Mockito.verify(requestBodyUriSpec, times(1)).uri(updateLoanStatementUrl, statementId);
        Mockito.verify(requestHeadersSpec, times(1)).retrieve();
        Mockito.verify(responseSpec, times(1)).onStatus(any(), any());
        Mockito.verify(responseSpec, times(1)).bodyToMono(Void.class);
    }

    @Test
    void sendTest_whenStatus400_thenThrowErrorProcessingRequestException() {
        String dealServiceUrl = "deal:9090";
        String updateLoanStatementUrl = "/deal/admin/statement/{statementId}/status";
        String statementId = "63e8f05d-6d53-49b0-a4f5-80939b31a0d8";

        when(dealMSProperties.getDealServerUrl()).thenReturn(dealServiceUrl);
        when(dealMSProperties.getUpdateLoanStatementUrl()).thenReturn(updateLoanStatementUrl);

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(updateLoanStatementUrl, statementId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(ErrorProcessingRequestException.class);

        Assertions.assertThrows(ErrorProcessingRequestException.class, () -> clientService.send(statementId));

        Mockito.verify(dealMSProperties, times(2)).getUpdateLoanStatementUrl();
        Mockito.verify(requestBodyUriSpec, times(1)).uri(updateLoanStatementUrl, statementId);
        Mockito.verify(requestHeadersSpec, times(1)).retrieve();
        Mockito.verify(responseSpec, times(1)).onStatus(any(), any());
    }

    @Test
    void sendTest_whenStatus409_thenThrowLoanRefusalException() {
        String dealServiceUrl = "deal:9090";
        String updateLoanStatementUrl = "/deal/admin/statement/{statementId}/status";
        String statementId = "63e8f05d-6d53-49b0-a4f5-80939b31a0d8";

        when(dealMSProperties.getDealServerUrl()).thenReturn(dealServiceUrl);
        when(dealMSProperties.getUpdateLoanStatementUrl()).thenReturn(updateLoanStatementUrl);

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(updateLoanStatementUrl, statementId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(LoanRefusalException.class);

        Assertions.assertThrows(LoanRefusalException.class, () -> clientService.send(statementId));

        Mockito.verify(dealMSProperties, times(2)).getUpdateLoanStatementUrl();
        Mockito.verify(requestBodyUriSpec, times(1)).uri(updateLoanStatementUrl, statementId);
        Mockito.verify(requestHeadersSpec, times(1)).retrieve();
        Mockito.verify(responseSpec, times(1)).onStatus(any(), any());
    }
}