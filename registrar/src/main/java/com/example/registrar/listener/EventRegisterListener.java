package com.example.registrar.listener;

import com.example.registrar.dto.EventMessage;
import com.example.registrar.model.RawEvent;
import com.example.registrar.repository.RawEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventRegisterListener {
    private final RawEventRepository rawEventRepository;

    @KafkaListener(
            topics = "events-topic",
            groupId = "registrar-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(EventMessage message) {
        RawEvent raw = new RawEvent();
        raw.setEventId(message.getId());
        raw.setSource(message.getSource());
        raw.setType(message.getType());
        raw.setTimestamp(message.getTimestamp());

        rawEventRepository.save(raw);

        log.info("Успешно получено сообщение: {}", message.getId());
    }
}