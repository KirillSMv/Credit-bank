package ru.development.Dossier;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.development.Dossier.model.EmailMessageDto;

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
    public ConsumerFactory<String, EmailMessageDto> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDossier");
        properties.put(TYPE_MAPPINGS, "ru.development.Dossier.model.EmailMessageDto:ru.development.Dossier.model.EmailMessageDto");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600_000);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, EmailMessageDto>> listenerContainerFactory(
            ConsumerFactory<String, EmailMessageDto> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }


}
