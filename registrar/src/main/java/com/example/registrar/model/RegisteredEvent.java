package com.example.registrar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "registered_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredEvent {
    @Id
    private UUID id;
    private String source;
    private String type;
    private LocalDateTime timestamp;
}