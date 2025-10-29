package com.mohit.expense_backend.unusedFfiles;

import com.mohit.expense_backend.unusedFfiles.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Returning a single Category
    Category findByName(String name);

}
