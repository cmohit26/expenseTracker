package com.mohit.expense_backend.IncomeStuffDeleteLATER;

import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/income")
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
        return incomeService.addIncome(user, dto);
    }

    // Get all incomes for a user
    @GetMapping("/me")
    public List<Income> getMyIncomes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return incomeService.getUserIncomes(userPrincipal.getUser());
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
}