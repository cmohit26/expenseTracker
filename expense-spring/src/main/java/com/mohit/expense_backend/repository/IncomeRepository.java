package com.mohit.expense_backend.repository;

import com.mohit.expense_backend.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUserId(Integer userId);

}
