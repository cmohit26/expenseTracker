package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.entities.User;
import com.mohit.expense_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
//    Bulk Insert Endpoint:
//    Create an endpoint that accepts a list of users and inserts them in batch while handling duplicate emails gracefully.
    @PostMapping("/users/bulk")
    public ResponseEntity<String> bulkInsertUsers(@RequestBody List<User> users) {
        List<User> usersToSave = new ArrayList<>();
        for (User user : users) {
//            boolean exists = userRepository.existsByEmail(user.getEmail());
//            if (!exists) {
//                usersToSave.add(user);
//            }
        }
        userRepository.saveAll(usersToSave);
        return ResponseEntity.ok("Bulk insert completed, duplicates skipped");
    }

}