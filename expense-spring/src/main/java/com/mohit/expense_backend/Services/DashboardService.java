package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Entities.Expense;
import com.mohit.expense_backend.Repositories.CategoryRepository;
import com.mohit.expense_backend.Repositories.ExpenseRepository;
import com.mohit.expense_backend.Repositories.IncomeRepository;
import com.mohit.expense_backend.Security.UserPrincipal;
import com.mohit.expense_backend.dto.DashboardDTOs.DashboardDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.MonthlyExpenseDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.RecentTransactionDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.YearlyExpenseDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public DashboardService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public DashboardDTO getDashboardData() {

        DashboardDTO dto = new DashboardDTO();

        //Getting Logged In User
        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        //whatever user is logged in
        Integer userId = principal.getUser().getId();

        //Get
        Double totalIncome = incomeRepository.getTotalIncome(userId);
        dto.setTotalIncome(totalIncome);

        //Get
        Double totalExpenses = expenseRepository.getTotalExpenses(userId);
        dto.setTotalExpenses(totalExpenses);

        dto.setCurrentBalance(totalIncome - totalExpenses);

//        Integer categoryCount = categoryRepository.getCategoryCount(userId);
//        dto.setCategoryCount(categoryCount);

        //Get the Recent Transactions
        List<Expense> recentExpenses = expenseRepository.findTop10ByUserIdOrderByDateDesc(userId);
        List<RecentTransactionDTO> transactions = new ArrayList<>();
            for (Expense expense : recentExpenses) {
                RecentTransactionDTO transaction =
                        new RecentTransactionDTO(
                                "Expense",
                                expense.getTitle(),
                                expense.getAmount(),
                                expense.getDate()
                        );
                transactions.add(transaction);
            }
        dto.setRecentTransactions(transactions);

        //FOR THE MONTHLY CHART (Monthly Expense)
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        Map<Month, Double> monthTotals =
                expenses.stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getDate().getMonth(),
                                Collectors.summingDouble(
                                        Expense::getAmount
                                )
                        ));
        List<MonthlyExpenseDTO> monthlyExpenses =
                Arrays.stream(Month.values())
                        .map(month ->
                                new MonthlyExpenseDTO(
                                        month.name().substring(0,3),
                                        monthTotals.getOrDefault(month,0.0)
                                ))
                        .collect(Collectors.toList());
        dto.setMonthlyExpenses(monthlyExpenses);

        //FOR THE MONTHLY CHART (Monthly Expense)
        List<YearlyExpenseDTO> yearlyExpenses =
                expenses.stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getDate().getYear(),
                                Collectors.groupingBy(
                                        e -> e.getDate().getMonth(),
                                        Collectors.summingDouble(Expense::getAmount)
                                )
                        ))
                        .entrySet()
                        .stream()
                        .map(yearEntry -> {
                            YearlyExpenseDTO yearlyDto = new YearlyExpenseDTO();
                            yearlyDto.setYear(yearEntry.getKey());
                            List<MonthlyExpenseDTO> months =
                                    yearEntry.getValue()
                                            .entrySet()
                                            .stream()
                                            .map(monthEntry ->
                                                    new MonthlyExpenseDTO(
                                                            monthEntry.getKey()
                                                                    .name()
                                                                    .substring(0, 3),
                                                            monthEntry.getValue()
                                                    )
                                            )
                                            .toList();
                            yearlyDto.setMonths(months);
                            return yearlyDto;
                        })
                        .toList();
        dto.setYearlyExpenses(yearlyExpenses);

        return dto;
    }

}