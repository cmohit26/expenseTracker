package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUser(User user);

    @Query("""
       SELECT COALESCE(SUM(i.amount),0)
       FROM Income i
       WHERE i.user.id = :userId
       AND i.status = 'APPROVED'
       """)
    Double getTotalIncome(Integer userId);

    List<Income> findByStatus(ApprovalStatus status);

}
