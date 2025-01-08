package ru.development.Dossier;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.reactive.function.client.WebClient;
import ru.development.Dossier.client.DealMSProperties;
import ru.development.Dossier.model.EmailMessageDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.clientId}")
    private String clientId;

    private final DealMSProperties dealMSProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ConsumerFactory<String, EmailMessageDto> consumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, EmailMessageDto.class.getName()); //"ru.development.Dossier.model.EmailMessageDto:ru.development.Dossier.model.EmailMessageDto");
        properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDossier");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600_000);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, EmailMessageDto>(properties);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(objectMapper));
        return kafkaConsumerFactory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, EmailMessageDto>> kafkaListenerContainerFactory(
            ConsumerFactory<String, EmailMessageDto> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public WebClient webClientConfig() {
        return WebClient.builder()
                .baseUrl(dealMSProperties.getDealServerUrl())
                .defaultHeaders(ApplicationConfig::headers)
                .build();
    }

    private static void headers(HttpHeaders httpHeaders) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }
}
