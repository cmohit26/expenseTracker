package com.mohit.expense_backend.repository;

import com.mohit.expense_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //Because expecting at most one result, and it might not exist.
    User findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    User findByFirstNameAndPassword(String firstName, String password);

//    User findByUsername(String username);
}
