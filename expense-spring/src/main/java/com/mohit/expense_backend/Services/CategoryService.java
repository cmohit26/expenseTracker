package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Category;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.CategoryRepository;
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

    public Category createCategory(Category category) {

        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        User user = principal.getUser();
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

    // ================= USER CATEGORIES =================
    public List<Category> getUserCategories() {
        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer userId = principal.getUser().getId();

        return categoryRepository.findByUserId(userId);
    }

    // ================= GLOBAL CATEGORIES =================
    public List<Category> getGlobalCategories() {
        return categoryRepository.findByGlobalCategoryTrue();
    }

    // ================= AVAILABLE (EXPENSE PAGE) =================
    public List<Category> getAvailableCategories() {
        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer userId = principal.getUser().getId();

        return categoryRepository.findByGlobalCategoryTrueOrUserId(userId);
    }

    public List<Category> getCategoriesForCurrentUser(){
        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        Integer userId = principal.getUser().getId();
        return categoryRepository.getAvailableCategories(userId);
    }

    public Category getCategoryById(Integer id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer userId = principal.getUser().getId();

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

        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer userId = principal.getUser().getId();
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

        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer userId = userPrincipal.getUser().getId();
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
