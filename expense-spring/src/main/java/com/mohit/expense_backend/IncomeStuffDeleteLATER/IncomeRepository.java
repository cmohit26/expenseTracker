package com.mohit.expense_backend.IncomeStuffDeleteLATER;

import com.mohit.expense_backend.IncomeStuffDeleteLATER.Income;
import com.mohit.expense_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUser(User user);

    List<Income> findByUserId(int userId);

}
