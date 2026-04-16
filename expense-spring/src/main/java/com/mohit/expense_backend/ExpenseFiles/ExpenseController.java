package com.mohit.expense_backend.ExpenseFiles;
import com.mohit.expense_backend.ExpenseFiles.Expense;
import com.mohit.expense_backend.ExpenseFiles.ExpenseService;
import com.mohit.expense_backend.dto.ExpenseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/expense")
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
    public List<Expense> getExpensesByUser(@PathVariable Integer userId) {
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

}

