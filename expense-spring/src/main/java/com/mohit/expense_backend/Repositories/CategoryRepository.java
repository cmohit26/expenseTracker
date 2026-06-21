package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

//    @Query("""
//       SELECT COUNT(c)
//       FROM Category c
//       WHERE c.user.id = :userId
//       """)
//    Integer getCategoryCount(Integer userId);
}
