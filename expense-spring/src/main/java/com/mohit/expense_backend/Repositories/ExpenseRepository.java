package com.mohit.expense_backend.Repositories;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {

    List<Expense> findByUserId(Integer userId);

    @Query("""
        SELECT e
        FROM Expense e
        JOIN FETCH e.category
        WHERE e.user.id = :userId
        ORDER BY e.date DESC
        """)
    List<Expense> findRecentExpenses(Integer userId, Pageable pageable);

    List<Expense> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

    Page<Expense> findByUserIdOrderByDateDesc(
            Integer userId,
            Pageable pageable
    );

    @Query("""
       SELECT COALESCE(SUM(e.amount),0)
       FROM Expense e
       WHERE e.user.id = :userId
       AND e.status = 'APPROVED'
       """)
    Double getTotalExpenses(Integer userId);

    @Query("""
        SELECT DISTINCT e
        FROM Expense e
        JOIN FETCH e.user
        JOIN FETCH e.category
        WHERE e.status = :status
        ORDER BY e.date DESC
        """)
    List<Expense> findByStatus(ApprovalStatus status);

    @Transactional          //So that it commits only when all the deletions are done
    @Modifying
    void deleteByUserId(Integer userId); //For USER delete, fixing FK constraint

}

