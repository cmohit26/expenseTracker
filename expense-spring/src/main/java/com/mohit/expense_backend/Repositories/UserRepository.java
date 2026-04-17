package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //Because expecting at most one result, and it might not exist.
    User findByEmail(String email);

    @Query("SELECT MAX(u.id) FROM User u")
    Optional<Long> findMaxId();

    Optional<User> findByFirstName(String firstName);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    User findByFirstNameAndPassword(String firstName, String password);


//    User findByUsername(String username);
}
