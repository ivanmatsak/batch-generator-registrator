package com.example.generator.controller;

import com.example.generator.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final EventRepository repository;

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return Map.of(
                "total", repository.count(),
                "processed", repository.countByProcessed(true),
                "pending", repository.countByProcessed(false)
        );
    }
}