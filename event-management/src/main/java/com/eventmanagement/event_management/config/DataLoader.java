package com.eventmanagement.event_management.config;

import com.eventmanagement.event_management.model.Event;
import com.eventmanagement.event_management.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataLoader(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (eventRepository.count() == 0) {
            Event e1 = new Event();
            e1.setTitle("Tech Fest 2025");
            e1.setDescription("A festival of technology and innovation");
            e1.setDate(LocalDate.of(2025, 9, 15));

            Event e2 = new Event();
            e2.setTitle("Music Concert");
            e2.setDescription("Live performances from top artists");
            e2.setDate(LocalDate.of(2025, 10, 5));

            Event e3 = new Event();
            e3.setTitle("Startup Expo");
            e3.setDescription("Showcase of emerging startups");
            e3.setDate(LocalDate.of(2025, 11, 20));

            eventRepository.saveAll(List.of(e1, e2, e3));
            System.out.println("✅ Demo events added to the database");
        }
    }
}
