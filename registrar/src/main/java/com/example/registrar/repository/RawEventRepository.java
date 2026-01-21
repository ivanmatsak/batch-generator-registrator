package com.example.registrar.repository;

import com.example.registrar.model.RawEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface RawEventRepository extends JpaRepository<RawEvent, Long> {
    @Transactional
    void deleteByEventId(UUID eventId);
}