/*
package ru.development.Dossier;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.development.Dossier.kafka.KafkaTopicsMessagesProperties;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {
    private final KafkaTopicsMessagesProperties properties;

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder.name(properties.getFinishRegistrationTopic()).partitions(1).replicas(1).build();

    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return TopicBuilder.name(properties.getCreateDocumentsTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return TopicBuilder.name(properties.getSendDocumentsTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder.name(properties.getSendSesCodeTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return TopicBuilder.name(properties.getCreditIssuedTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic statementDeniedTopic() {
        return TopicBuilder.name(properties.getStatementDeniedTopic()).partitions(1).replicas(1).build();
    }
}
*/
