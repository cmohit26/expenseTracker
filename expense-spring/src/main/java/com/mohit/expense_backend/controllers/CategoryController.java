package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.Entities.Category;
import com.mohit.expense_backend.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // ================= USER PAGE =================
    @GetMapping("/user")
    public List<Category> getUserCategories() {
        return categoryService.getUserCategories();
    }

    // ================= ADMIN PAGE =================
    @GetMapping("/global")
    public List<Category> getGlobalCategories() {
        return categoryService.getGlobalCategories();
    }

    // ================= EXPENSE PAGE =================
    @GetMapping("/available")
    public List<Category> getAvailableCategories() {
        return categoryService.getAvailableCategories();
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Integer id,
                                   @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "Category deleted successfully";
    }
}