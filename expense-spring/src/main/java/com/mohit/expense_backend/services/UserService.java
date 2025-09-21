package com.mohit.expense_backend.services;


import com.mohit.expense_backend.entities.User;
import com.mohit.expense_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No user with ID " + id + " found"
                ));
    }

    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Added";
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User deleteUser(Integer id) {
        userRepository.deleteById(id);
        return null;
    }

    public List<User> getSortedUsersBy(String field) {
        switch (field) {
            case "id":
            case "firstName":
            case "email":
            case "roles":
                return userRepository.findAll(Sort.by(Sort.Direction.DESC, field));
            default:
                return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
    }

    public List<User> searchUsers(String search) {
        return userRepository.findByFirstNameContainingIgnoreCase(search);
    }

    public boolean authenticateUser(String email, String password){
        User user = userRepository.findByEmail(email);

        if (user == null || !user.getEmail().equals(email)) {
            throw new UsernameNotFoundException("User does not exist");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return true;
    }

}
