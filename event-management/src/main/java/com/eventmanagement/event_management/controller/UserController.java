package com.eventmanagement.event_management.controller;

import com.eventmanagement.event_management.model.User;
import com.eventmanagement.event_management.model.Event;
import com.eventmanagement.event_management.repository.UserRepository;
import com.eventmanagement.event_management.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EventRepository eventRepo;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "User already exists. Please login."));
        }
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "message", "User registered successfully"));
    }

    // Login existing user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "error", "message", "Invalid email or password"));
    }

        // Get current logged-in user (OAuth or Normal)



    // Register for Event
    @PostMapping("/registerEvent/{eventId}")
    public ResponseEntity<?> registerForEvent(@PathVariable Long eventId,
                                              @RequestParam String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        Optional<Event> eventOpt = eventRepo.findById(eventId);

        if (userOpt.isEmpty() || eventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "User or Event not found"));
        }

        User user = userOpt.get();
        Event event = eventOpt.get();

        user.getRegisteredEvents().add(event);
        userRepo.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Registered for event successfully",
                "eventsAttended", user.getRegisteredEvents().size()
        ));
    }

    @GetMapping("/eventsAttended")
    public ResponseEntity<Map<String, Object>> getEventsAttended(@RequestParam String email) {
        return userRepo.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of(
                        "eventsAttended", (Object) user.getRegisteredEvents().size() // Cast to Object
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", (Object) "error",
                                "message", "User not found"
                        )));
    }
}
