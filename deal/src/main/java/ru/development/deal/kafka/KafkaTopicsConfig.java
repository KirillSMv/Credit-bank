package ru.development.deal.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    private final KafkaTopicsMessagesProperties properties;

    @Autowired
    public KafkaTopicsConfig(KafkaTopicsMessagesProperties properties) {
        this.properties = properties;
    }

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
