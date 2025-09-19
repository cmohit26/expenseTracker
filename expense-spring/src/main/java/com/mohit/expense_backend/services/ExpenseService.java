package com.mohit.expense_backend.services;

import com.mohit.expense_backend.entities.Expense;
import com.mohit.expense_backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUser(Integer userId) {
        return expenseRepository.findByUserId(userId);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public Expense updateExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getExpensesByCategory(Integer userId, Integer categoryId) {
        return expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

    public List<Expense> getRecentExpenses(Integer userId, int limit) {
        // For this to work, make sure your repository has a method like:
        // List<Expense> findTop10ByUserIdOrderByDateDesc(Integer userId);
        return expenseRepository.findTop10ByUserIdOrderByDateDesc(userId); // adjust limit in repo if needed
    }
}
