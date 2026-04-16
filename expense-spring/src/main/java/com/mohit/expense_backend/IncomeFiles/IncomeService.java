package com.mohit.expense_backend.IncomeFiles;

import com.mohit.expense_backend.UserFiles.User;
import com.mohit.expense_backend.UserFiles.UserRepository;
import com.mohit.expense_backend.dto.IncomeRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public Income addIncome(User user, IncomeRequestDTO dto) {

        Income income = new Income();
        income.setAmount(dto.getAmount());
        income.setSource(dto.getSource());
        income.setDate(LocalDate.parse(dto.getDate()));
        income.setUser(user);

        return incomeRepository.save(income);
    }

    public List<Income> getUserIncomes(User user) {
        return incomeRepository.findByUser(user);
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
