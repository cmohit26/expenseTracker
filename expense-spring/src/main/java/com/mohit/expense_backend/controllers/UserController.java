package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.entities.User;
import com.mohit.expense_backend.repository.UserRepository;
import com.mohit.expense_backend.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.StringJoiner;

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
        return ResponseEntity.ok("pong");
    }

    //Works
    @GetMapping("getUser/{id}")
    public User getUser(@PathVariable Integer id){
        System.out.println("Getting user for "+ id);
        return userService.getUserById(id);
    }

    //WORKS
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        System.out.println("Deleting user with ID: " + id);
//        log.info("jhgjh");
        userService.deleteUser(id);
        return "redirect:/admin/userInfo"; // go back to admin page
    }

    //TestAgain
    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, @ModelAttribute User user) {
        user.setId(id); // Ensure the ID from URL is used
        userService.updateUser(user);
        return "redirect:/admin/userInfo"; // Redirect to your main page or user list
    }

    //Works
    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        System.out.println("Getting all users");
        return userService.getAllUsers();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(HttpSession session, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    //WORKS
    @GetMapping("/sortUsersBy/{field}")
    public List<User> sortUsers(@PathVariable String field, Model model) {
        List<User> sortedUsers = userService.getSortedUsersBy(field);
        System.out.println("Based on field : "+ field + "|| Sorted users are: " + sortedUsers);
        model.addAttribute("users", sortedUsers);
        return sortedUsers;
        //Supposed to return this for this page to work
        //return "AdminPages/userInfo"; // your Thymeleaf template namez
    }

    //WORKS
    @GetMapping("/searchUsers")
    public List<User> searchUsers(@RequestParam(value = "search", required = false) String search) {
        List<User> users;
        System.out.println("Searching with" + search);
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search); // Search users based on input
        } else {
            users = userService.getAllUsers(); // If search is empty, show all users
        }
        System.out.println("Users Data: " + users);
        return users;
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
