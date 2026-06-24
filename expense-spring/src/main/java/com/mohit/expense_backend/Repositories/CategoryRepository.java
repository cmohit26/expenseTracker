package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    @Query("""
    SELECT c
    FROM Category c
    WHERE c.globalCategory = true OR c.user.id = :userId
    ORDER BY c.name
    """)
    List<Category> getAvailableCategories(Integer userId);

    List<Category> findByUserId(Integer userId);

    List<Category> findByGlobalCategoryTrue();

    List<Category> findByGlobalCategoryTrueOrUserId(Integer userId);

//    @Query("""
//       SELECT COUNT(c)
//       FROM Category c
//       WHERE c.user.id = :userId
//       """)
//    Integer getCategoryCount(Integer userId);
}
