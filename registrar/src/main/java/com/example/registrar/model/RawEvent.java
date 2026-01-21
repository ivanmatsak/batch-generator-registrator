package com.example.registrar.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "raw_events")
@Data
public class RawEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID eventId;
    private String source;
    private String type;
    private LocalDateTime timestamp;
}