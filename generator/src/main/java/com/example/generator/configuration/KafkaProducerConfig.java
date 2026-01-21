package com.example.generator.configuration;

import com.example.generator.dto.EventMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, EventMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        var keySerializer = new StringSerializer();
        var valueSerializer = new JacksonJsonSerializer<EventMessage>();

        return new DefaultKafkaProducerFactory<>(
                configProps,
                keySerializer,
                valueSerializer
        );
    }

    @Bean
    public KafkaTemplate<String, EventMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}