package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Repositories.IncomeRepository;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.UserRepository;
import com.mohit.expense_backend.Security.CurrentUserService;
import com.mohit.expense_backend.dto.IncomeRequestDTO;
import com.mohit.expense_backend.dto.PendingIncomeDTO;
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
        income.setStatus(ApprovalStatus.PENDING);

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

    public List<PendingIncomeDTO> findPendingIncomes() {

        return incomeRepository.findByStatus(ApprovalStatus.PENDING)
                .stream()
                .map(income -> new PendingIncomeDTO(
                        income.getId().intValue(),
                        income.getSource(),
                        income.getAmount(),
                        income.getDate(),
                        income.getUser().getFirstName(),
                        income.getUser().getLastName(),
                        income.getStatus(),
                        income.getRejectionReason()
                ))
                .toList();
    }

    public Income approveIncome(int id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        income.setStatus(ApprovalStatus.APPROVED);
        return incomeRepository.save(income);
    }

    public Income rejectIncome(int id, String reason) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        income.setStatus(ApprovalStatus.REJECTED);
        income.setRejectionReason(reason);
        return incomeRepository.save(income);
    }
}
