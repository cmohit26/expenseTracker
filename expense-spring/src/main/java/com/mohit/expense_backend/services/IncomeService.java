package com.mohit.expense_backend.services;

import com.mohit.expense_backend.entities.Income;
import com.mohit.expense_backend.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<Income> getIncomeByUser(Integer userId) {
        return incomeRepository.findByUserId(userId);
    }

    public Income getIncomeById(Integer id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
    }

    public Income updateIncome(Income income) {
        return incomeRepository.save(income);
    }

    public void deleteIncome(Integer id) {
        incomeRepository.deleteById(id);
    }
}
