package com.example.generator.repository;

import com.example.generator.model.OutgoingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<OutgoingEvent, UUID> {
    long countByProcessed(boolean processed);
}
