package com.eventmanagement.event_management.repository;

import com.eventmanagement.event_management.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizerId(Long userId);

    Optional<Event> findByTitle(String eventName);
}


