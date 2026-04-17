package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUser(User user);


}
