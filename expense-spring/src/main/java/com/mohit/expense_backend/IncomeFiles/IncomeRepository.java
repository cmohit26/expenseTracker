package com.mohit.expense_backend.IncomeFiles;

import com.mohit.expense_backend.UserFiles.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUser(User user);


}
