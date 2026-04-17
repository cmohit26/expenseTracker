package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.Repositories.UserRepository;
import com.mohit.expense_backend.dto.UserRequestDTO;
import com.mohit.expense_backend.dto.UserResponseDTO;
import com.mohit.expense_backend.Services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("/v1/user/{id}")
    public UserResponseDTO getUser(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/v1/user/{id}")
    public String deleteUser(@PathVariable Integer id) {
        System.out.println("Deleting user with ID: " + id);
        userService.deleteUser(id);
        return "redirect:/admin/userInfo"; // go back to admin page
    }

    @PutMapping("/v1/user/{id}")
    public String editUser(@PathVariable Integer id, @ModelAttribute User user) {
        user.setId(id); // Ensure the ID from URL is used
        userService.updateUser(user);
        return "redirect:/admin/userInfo"; // Redirect to your main page or user list
    }

    @PostMapping("/v1/user")
    public ResponseEntity<?> addUser(@RequestBody UserRequestDTO dto) {
        userService.addUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/v1/users")
    public List<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/v1/users/sortBy/{field}")
    public List<UserResponseDTO> sortUsers(@PathVariable String field) {
        return userService.getSortedUsersBy(field);
    }

    @GetMapping("/v1/users/search")
    public List<UserResponseDTO> searchUsers(
            @RequestParam(value = "search", required = false) String search) {

        System.out.println("Searching with " + search);

        if (search != null && !search.trim().isEmpty()) {
            return userService.searchUsers(search);
        } else {
            return userService.getAllUsers();
        }
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());

        return ResponseEntity.ok(userService.mapToResponseDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession httpSession){
        try{
            boolean isAuthenticated = userService.authenticateUser(user.getEmail(), user.getPassword());

            if(isAuthenticated){
                httpSession.setAttribute("user", user.getFirstName());
                return ResponseEntity.ok("Login Was Successfull");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occurred in login Controller");
        }
    }

}
