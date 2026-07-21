package com.mohit.expense_backend.controllers;
import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Expense;
import com.mohit.expense_backend.Services.ExpenseServices.ExpenseService;
import com.mohit.expense_backend.dto.ExpenseDTO;
import com.mohit.expense_backend.dto.PendingExpenseDTO;
import com.mohit.expense_backend.dto.RejectionRequestDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/expense")
@EnableMethodSecurity
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Create
    @PostMapping
    public Expense createExpense(@RequestBody ExpenseDTO expenseDTO) {
        return expenseService.createExpense(expenseDTO);
    }

    // Get all expenses of user
    @GetMapping
    public List<Expense> getExpensesByUser() {
        return expenseService.getExpensesByUser();
    }

    // Get recent 10
    @GetMapping("/recent")
    public List<Expense> getRecentExpenses() {
        return expenseService.getRecentExpenses();
    }

    // Filter by category
    @GetMapping("/user/{userId}/category/{categoryId}")
    public List<Expense> getByCategory(
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {
        return expenseService.getExpensesByCategory(userId, categoryId);
    }

    // Get one
    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable Integer id) {
        return expenseService.getExpenseById(id);
    }

    // Update
    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Integer id,
            @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Integer id) {
        expenseService.deleteExpense(id);
        return "Expense deleted successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending-expenses")
    public List<PendingExpenseDTO> getPendingExpenses() {
        return expenseService.findPendingExpenses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve-expense/{id}")
    public Expense approveExpense(@PathVariable Integer id) {
        return expenseService.approveExpense(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject-expense/{id}")
    public Expense rejectExpense(
            @PathVariable Integer id,
            @RequestBody RejectionRequestDTO dto) {
            return expenseService.rejectExpense(id, dto.getReason());
    }

}

