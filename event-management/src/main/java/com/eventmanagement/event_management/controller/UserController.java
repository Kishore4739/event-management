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
@RequestMapping("/api/users")
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

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(OAuth2AuthenticationToken token,
                                                              @RequestParam(required = false) String email) {
        Optional<User> foundUser = Optional.empty();

        if (token != null && token.isAuthenticated()) {
            String oauthEmail = token.getPrincipal().getAttribute("email");
            foundUser = userRepo.findByEmail(oauthEmail);
        } else if (email != null) {
            foundUser = userRepo.findByEmail(email);
        }

        return foundUser
                .map(user -> ResponseEntity.ok(Map.of("status", "success", "user", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "User not found")));
    }



    @PostMapping("/registerEvent")
    public ResponseEntity<?> registerForEvent(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String eventName = request.get("eventName");

        // ✅ Validate input
        if (email == null || password == null || eventName == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Email, password, and event name are required"));
        }

        // ✅ Find user
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "User not found"));
        }

        User user = userOpt.get();

        // ✅ Check password
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid password"));
        }

        // ✅ Find event
        Optional<Event> eventOpt = eventRepo.findByTitle(eventName);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "Event not found"));
        }

        Event event = eventOpt.get();

        // ✅ Register user for event
        user.getRegisteredEvents().add(event);
        userRepo.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Registered for event: " + eventName,
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
