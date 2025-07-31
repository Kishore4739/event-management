package com.eventmanagement.event_management.controller;


import com.eventmanagement.event_management.model.Event;
import com.eventmanagement.event_management.model.User;
import com.eventmanagement.event_management.repository.EventRepository;
import com.eventmanagement.event_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserRepository userRepo;

    // Create new event
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Map<String, String> body) {
        try {
            Long userId = Long.parseLong(body.get("userId")); // organizer id
            Optional<User> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Event event = new Event();
            event.setTitle(body.get("title"));
            event.setDescription(body.get("description"));
            event.setDate(LocalDate.parse(body.get("date")));
            event.setTime(LocalTime.parse(body.get("time")));
            event.setLocation(body.get("location"));
            event.setTicketPrice(Double.parseDouble(body.get("ticketPrice")));
            event.setOrganizer(userOpt.get());

            eventRepo.save(event);
            return ResponseEntity.ok("Event created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        }
    }

    // Get all events (public listing)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    // Get events by organizer
    @GetMapping("/organizer/{userId}")
    public List<Event> getEventsByOrganizer(@PathVariable Long userId) {
        return eventRepo.findByOrganizerId(userId);
    }

    // Get single event by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepo.findById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
    }

}

