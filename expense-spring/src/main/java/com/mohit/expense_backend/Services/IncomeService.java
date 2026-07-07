package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Repositories.IncomeRepository;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.UserRepository;
import com.mohit.expense_backend.Security.CurrentUserService;
import com.mohit.expense_backend.dto.IncomeRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;

    public IncomeService(IncomeRepository incomeRepository, CurrentUserService currentUserService) {
        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
    }

    public Income addIncome(IncomeRequestDTO dto) {

        User user = currentUserService.getCurrentUser();

        Income income = new Income();
        income.setAmount(dto.getAmount());
        income.setSource(dto.getSource());
        income.setDate(LocalDate.parse(dto.getDate()));
        income.setUser(user);

        return incomeRepository.save(income);
    }

    public List<Income> getUserIncomes() {
        User user = currentUserService.getCurrentUser();
        return incomeRepository.findByUser(user);
    }

    public Double getTotalIncome() {
        Integer userId = currentUserService.getCurrentUserId();
        return incomeRepository.getTotalIncome(userId);
    }

    public Income updateIncome(int incomeId, Income updatedIncome) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        income.setAmount(updatedIncome.getAmount());
        income.setDate(updatedIncome.getDate());
        income.setSource(updatedIncome.getSource());

        return incomeRepository.save(income);
    }

    public void deleteIncome(int incomeId) {
        incomeRepository.deleteById(incomeId);
    }
}
