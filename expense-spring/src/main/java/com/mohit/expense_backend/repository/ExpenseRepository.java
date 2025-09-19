package com.mohit.expense_backend.repository;

import com.mohit.expense_backend.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByUserId(Integer userId);

    List<Expense> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

    List<Expense> findTop10ByUserIdOrderByDateDesc(Integer userId);
}

