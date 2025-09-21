package com.mohit.expense_backend.controllers;
import com.mohit.expense_backend.entities.Expense;
import com.mohit.expense_backend.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Add a new expense
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    // Get expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Integer id) {
        try {
            Expense expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all expenses by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Integer userId) {
        List<Expense> expenses = expenseService.getExpensesByUser(userId);
        return ResponseEntity.ok(expenses);
    }


    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<Expense>> getRecentExpenses(@PathVariable Integer userId, @RequestParam(defaultValue = "10") int limit) {
        // Get recent expenses (you can adjust the limit as needed)
        List<Expense> expenses = expenseService.getRecentExpenses(userId, limit);
        return ResponseEntity.ok(expenses);
    }

    // Update an expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Integer id, @RequestBody Expense expense) {
        expense.setId(id); // Ensure the ID from URL is used
        Expense updatedExpense = expenseService.updateExpense(expense);
        return ResponseEntity.ok(updatedExpense);
    }

    // Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Integer id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }
    }

    // Get expenses by category and user ID
    //    @GetMapping("/user/{userId}/category/{categoryId}")
    //    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable Integer userId, @PathVariable Integer categoryId) {
    //        List<Expense> expenses = expenseService.getExpensesByCategory(userId, categoryId);
    //        return ResponseEntity.ok(expenses);
    //    }

}

