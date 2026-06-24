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
import java.util.*;
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
        Map<Month, Double> monthTotals = new HashMap<>();
            for (Expense expense : expenses) {
                Month month = expense.getDate().getMonth();
                Double currentTotal = monthTotals.get(month);
                if (currentTotal == null) {
                    currentTotal = 0.0;
                }
                monthTotals.put(month, currentTotal + expense.getAmount());
            }
        List<MonthlyExpenseDTO> monthlyExpenses = new ArrayList<>();
            for (Month month : Month.values()) {
                String monthName = month.name().substring(0, 3);
                Double total = monthTotals.get(month);
                if (total == null) {
                    total = 0.0;
                }
                monthlyExpenses.add(
                        new MonthlyExpenseDTO(monthName, total)
                );
            }
        dto.setMonthlyExpenses(monthlyExpenses);

        //FOR THE MONTHLY CHART (Monthly Expense)
        Map<Integer, Map<Month, Double>> yearlyMap = new HashMap<>();
            for (Expense expense : expenses) {
                int year = expense.getDate().getYear();
                Month month = expense.getDate().getMonth();
                double amount = expense.getAmount();
                Map<Month, Double> monthMap = yearlyMap.get(year);
                if (monthMap == null) {
                    monthMap = new HashMap<>();
                    yearlyMap.put(year, monthMap);
                }
                double current = monthMap.getOrDefault(month, 0.0);
                monthMap.put(month, current + amount);
            }
        List<YearlyExpenseDTO> yearlyExpenses = new ArrayList<>();
            for (Map.Entry<Integer, Map<Month, Double>> yearEntry : yearlyMap.entrySet()) {
                YearlyExpenseDTO yearlyDto = new YearlyExpenseDTO();
                yearlyDto.setYear(yearEntry.getKey());
                List<MonthlyExpenseDTO> months = new ArrayList<>();
                // keeping month order consistent (JAN–DEC)
                for (Month month : Month.values()) {
                    Double total = yearEntry.getValue().get(month);
                    if (total != null) {
                        months.add(
                                new MonthlyExpenseDTO(
                                        month.name().substring(0, 3),
                                        total
                                )
                        );
                    }
                }
                yearlyDto.setMonths(months);
                yearlyExpenses.add(yearlyDto);
            }
        dto.setYearlyExpenses(yearlyExpenses);

        return dto;
    }

}