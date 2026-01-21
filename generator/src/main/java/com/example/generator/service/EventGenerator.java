package com.example.generator.service;

import com.example.generator.dto.EventMessage;
import com.example.generator.model.OutgoingEvent;
import com.example.generator.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventGenerator {
    private final EventRepository repository;
    private final KafkaTemplate<String, EventMessage> kafkaTemplate;

    @Value("${spring.application.name}")
    private String serviceName;

    @Scheduled(fixedRate = 5000)
    public void generateEvent() {
        var event = new OutgoingEvent(UUID.randomUUID(), "USER_ACTION", serviceName, false, LocalDateTime.now());
        repository.save(event);
        kafkaTemplate.send("events-topic", new EventMessage(event.getId(), serviceName, event.getType(), "Data", event.getCreatedAt()));
        log.info("Event generated");
    }

    @KafkaListener(topics = "confirmations-topic", groupId = "generator-group")
    public void handleConfirmation(String eventId) {
        repository.findById(UUID.fromString(eventId.replace("\"", ""))).ifPresent(e -> {
            e.setProcessed(true);
            repository.save(e);
            log.info("Event {} processed", eventId);
        });
    }
}