package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.Services.IncomeService;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.dto.IncomeRequestDTO;
import com.mohit.expense_backend.dto.RejectionRequestDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/income")
@EnableMethodSecurity
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public Income addIncome(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody IncomeRequestDTO dto) {
        User user = userPrincipal.getUser();
        return incomeService.addIncome(dto);
    }

    // Get all incomes for a user
    @GetMapping("/me")
    public List<Income> getMyIncomes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return incomeService.getUserIncomes();
    }

    // Update income
    @PutMapping("/{incomeId}")
    public Income updateIncome(@PathVariable int incomeId, @RequestBody Income income) {
        return incomeService.updateIncome(incomeId, income);
    }

    // Delete income
    @DeleteMapping("/{incomeId}")
    public void deleteIncome(@PathVariable int incomeId) {
        incomeService.deleteIncome(incomeId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending-incomes")
    public List<Income> getPendingIncomes() {
        return incomeService.findPendingIncomes();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve-income/{id}")
    public Income approveIncome(@PathVariable Integer id) {
        return incomeService.approveIncome(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject-income/{id}")
    public Income rejectIncome(@PathVariable Integer id,
                               @RequestBody RejectionRequestDTO dto) {
        return incomeService.rejectIncome(id, dto.getReason());
    }
}