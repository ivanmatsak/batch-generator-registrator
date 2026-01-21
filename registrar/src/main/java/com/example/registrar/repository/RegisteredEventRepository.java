package com.example.registrar.repository;

import com.example.registrar.model.RegisteredEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface RegisteredEventRepository extends JpaRepository<RegisteredEvent, UUID> {
    Page<RegisteredEvent> findAllByTimestampBetweenAndTypeAndSource(
            LocalDateTime start,
            LocalDateTime end,
            String type,
            String source,
            Pageable pageable
    );
}
