package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

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

    List<Category> findAll();      // already inherited from JpaRepository

    @Transactional      //So that it commits only when all the deletions are done
    @Modifying
    void deleteByUserId(Integer userId);        //For USER delete, fixing FK constraint

}
