package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Category;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.CategoryRepository;
import com.mohit.expense_backend.Security.CurrentUserService;
import com.mohit.expense_backend.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CurrentUserService currentUserService;

    public Category createCategory(Category category) {

        User user = currentUserService.getCurrentUser();

//        String role = user.getRoles();

        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }

        // ADMIN can create global categories
        if (category.isGlobalCategory()) {
//            if (!role.contains("ADMIN")) {
//                throw new RuntimeException("Only admin can create global categories");
//            }
            category.setUser(null);
        } else {
            category.setUser(user);
        }

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {

        // only verify someone is logged in
        currentUserService.getCurrentUser();

        return categoryRepository.findAll();
    }

    // ================= USER CATEGORIES =================
    public List<Category> getUserCategories() {

        Integer userId = currentUserService.getCurrentUserId();

        return categoryRepository.findByUserId(userId);
    }

    // ================= GLOBAL CATEGORIES =================
    public List<Category> getGlobalCategories() {
        return categoryRepository.findByGlobalCategoryTrue();
    }

    // ================= AVAILABLE (EXPENSE PAGE) =================
    public List<Category> getAvailableCategories() {

        Integer userId = currentUserService.getCurrentUserId();

        return categoryRepository.findByGlobalCategoryTrueOrUserId(userId);
    }

    public List<Category> getCategoriesForCurrentUser(){

        Integer userId = currentUserService.getCurrentUserId();

        return categoryRepository.getAvailableCategories(userId);
    }

    public Category getCategoryById(Integer id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Integer userId = currentUserService.getCurrentUserId();

        boolean accessible =
                category.isGlobalCategory()
                        || (category.getUser() != null
                        && Objects.equals(category.getUser().getId(), userId));

        if (!accessible) {
            throw new RuntimeException("Access denied");
        }

        return category;
    }

    public Category updateCategory(Integer id, Category category) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Integer userId = currentUserService.getCurrentUserId();

//        String role = principal.getUser().getRoles();

        boolean isOwner = existing.getUser() != null
                && Objects.equals(existing.getUser().getId(), userId);

        boolean isGlobal = existing.isGlobalCategory();

        if (!isOwner && !isGlobal) {
            throw new RuntimeException("Not allowed to update category");
        }

        existing.setName(category.getName());

        return categoryRepository.save(existing);
    }

    public void deleteCategory(Integer id) {

        Integer userId = currentUserService.getCurrentUserId();

//        String role = userPrincipal.getUser().getRoles();

        Category category = getCategoryById(id);

        boolean isOwner = category.getUser() != null
                && Objects.equals(category.getUser().getId(), userId);

        boolean isGlobal = category.isGlobalCategory();

//        boolean isAdmin = role.contains("ADMIN");

        if (isOwner || !isGlobal) {
            categoryRepository.delete(category);
        } else {
            throw new RuntimeException("Not allowed to delete this category");
        }
    }
}
