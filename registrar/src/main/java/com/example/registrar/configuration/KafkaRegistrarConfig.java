package com.example.registrar.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaRegistrarConfig {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, UUID> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        StringSerializer keySerializer = new StringSerializer();
        JacksonJsonSerializer<UUID> valueSerializer = new JacksonJsonSerializer<>();

        return new DefaultKafkaProducerFactory<>(
                configProps,
                keySerializer,
                valueSerializer
        );
    }

    @Bean
    public KafkaTemplate<String, UUID> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}