package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Expense;
import com.mohit.expense_backend.Repositories.ExpenseRepository;
import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.dto.ExpenseDTO;
import com.mohit.expense_backend.Entities.Category;
import com.mohit.expense_backend.Repositories.CategoryRepository;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Expense createExpense(ExpenseDTO dto) {
        UserPrincipal userPrincipal = (UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setCategory(category);
        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUser() {
        UserPrincipal userPrincipal = (UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer userId = userPrincipal.getUser().getId();

        return expenseRepository.findByUserIdOrderByDateDesc(userId);
    }

    public List<Expense> getRecentExpenses() {

        UserPrincipal userPrincipal = (UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer userId = userPrincipal.getUser().getId();

        return expenseRepository.findTop10ByUserIdOrderByDateDesc(userId);
    }

    public List<Expense> getExpensesByCategory(Integer userId, Integer categoryId) {
        return expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

    public Expense updateExpense(Integer id, Expense updatedExpense) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setDate(updatedExpense.getDate());

        return expenseRepository.save(existing);
    }

    public void deleteExpense(Integer id) {

        UserPrincipal userPrincipal = (UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer userId = userPrincipal.getUser().getId();

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (expense.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        expenseRepository.delete(expense);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }
}