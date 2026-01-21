package com.example.registrar.controller;

import com.example.registrar.model.RegisteredEvent;
import com.example.registrar.repository.RegisteredEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventRegistrationController {
    private final RegisteredEventRepository repository;

    @GetMapping
    public String viewEvents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "source", required = false) String source,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RegisteredEvent> eventsPage;

        try {
            if (isNotBlank(start) && isNotBlank(end) && isNotBlank(type) && isNotBlank(source)) {
                LocalDateTime startDate = LocalDateTime.parse(start);
                LocalDateTime endDate = LocalDateTime.parse(end);
                eventsPage = repository.findAllByTimestampBetweenAndTypeAndSource(
                        startDate, endDate, type, source, pageable);
            } else {
                eventsPage = repository.findAll(pageable);
            }
        } catch (Exception e) {
            log.error("Ошибка парсинга фильтров, отдаем все данные: {}", e.getMessage());
            eventsPage = repository.findAll(pageable);
        }

        model.addAttribute("eventsPage", eventsPage);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("type", type);
        model.addAttribute("source", source);

        return "events";
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}