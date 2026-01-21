package com.example.generator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outgoing_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingEvent {
    @Id
    private UUID id;
    private String type;
    private String sourceName;
    private boolean processed;
    private LocalDateTime createdAt;
}