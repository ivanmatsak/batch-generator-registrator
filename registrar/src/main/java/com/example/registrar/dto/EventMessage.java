package com.example.registrar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage {
    private UUID id;
    private String source;
    private String type;
    private String payload;
    private LocalDateTime timestamp;
}