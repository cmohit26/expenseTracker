package com.mohit.expense_backend.Services;


import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.UserRepository;
import com.mohit.expense_backend.dto.UserRequestDTO;
import com.mohit.expense_backend.dto.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    public UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        dto.setFullName(convertToFullName(user.getFirstName(), user.getLastName()));
        dto.setAge(calculateAge(user.getDateOfBirth()));

        //because of frontend
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());

        return dto;
    }

    private User mapToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(dto.getRoles());
        user.setDateOfBirth(dto.getDateOfBirth());
        return user;
    }

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No user with ID " + id + " found"
                ));

        return mapToResponseDTO(user);
    }

    public String addUser(UserRequestDTO dto) {
        User user = mapToEntity(dto);
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

    public List<UserResponseDTO> getSortedUsersBy(String field) {

        if(field.equalsIgnoreCase("age")){
            return userRepository.findAll(Sort.by(Sort.Direction.DESC, "dateOfBirth"))
                    .stream()
                    .map(this::mapToResponseDTO)
                    .toList();
        }

        return userRepository.findAll(Sort.by(Sort.Direction.ASC, field))
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public List<UserResponseDTO> searchUsers(String search) {
        return userRepository.findByFirstNameContainingIgnoreCase(search)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
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

    private int calculateAge(LocalDate dob) {
        if (dob == null) return 0;
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private String convertToFullName(String fname, String lname) {
        StringBuilder fullName = new StringBuilder();

        if (fname != null) fullName.append(fname).append(" ");
        if (lname != null) fullName.append(lname);

        return fullName.toString().trim();
    }

}
