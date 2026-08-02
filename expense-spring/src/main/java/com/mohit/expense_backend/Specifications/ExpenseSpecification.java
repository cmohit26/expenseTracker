package com.mohit.expense_backend.Specifications;


import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Expense;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ExpenseSpecification {
    public static Specification<Expense> filterExpenses( Integer userId, ApprovalStatus status,
                                     Integer categoryId, LocalDate startDate, LocalDate endDate ){

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Always filter by logged-in user
            predicates.add(
                    cb.equal(
                            root.get("user").get("id"),
                            userId
                    )
            );
            // Status filter
            if(status != null){
                predicates.add(
                        cb.equal(
                                root.get("status"),
                                status
                        )
                );
            }
            // Category filter
            if(categoryId != null){
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );
            }
            // Start date
            if(startDate != null){
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("date"),
                                startDate
                        )
                );
            }
            // End date
            if(endDate != null){
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("date"),
                                endDate
                        )
                );
            }
            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}