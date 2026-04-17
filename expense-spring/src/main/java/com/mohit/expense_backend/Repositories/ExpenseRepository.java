package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByUserId(Integer userId);

    List<Expense> findTop10ByUserIdOrderByDateDesc(Integer userId);

    List<Expense> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

    List<Expense> findByUserIdOrderByDateDesc(Integer userId);
}

