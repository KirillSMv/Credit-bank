package ru.development.deal.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.development.deal.model.dto.EmailMessageDto;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS;

@Configuration
public class ApplicationConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.clientId}")
    private String clientId;

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ProducerFactory<String, EmailMessageDto> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(TYPE_MAPPINGS, "ru.development.Deal.model.dto.EmailMessageDto:ru.development.Deal.model.dto.EmailMessageDto");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        var defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<String, EmailMessageDto>(properties);
        defaultKafkaProducerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
        return defaultKafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, EmailMessageDto> kafkaTemplate(ProducerFactory<String, EmailMessageDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
