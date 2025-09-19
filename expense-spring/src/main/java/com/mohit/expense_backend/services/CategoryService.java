package com.mohit.expense_backend.services;

import com.mohit.expense_backend.entities.Category;
import com.mohit.expense_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
