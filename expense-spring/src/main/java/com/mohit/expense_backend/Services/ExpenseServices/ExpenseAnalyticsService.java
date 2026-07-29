package com.mohit.expense_backend.Services.ExpenseServices;

import com.mohit.expense_backend.Entities.Expense;
import com.mohit.expense_backend.Repositories.CategoryRepository;
import com.mohit.expense_backend.Repositories.ExpenseRepository;
import com.mohit.expense_backend.Security.CurrentUserService;
import com.mohit.expense_backend.dto.DashboardDTOs.CategoryExpenseDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.MonthlyExpenseDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.RecentTransactionDTO;
import com.mohit.expense_backend.dto.DashboardDTOs.YearlyExpenseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseAnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    //Either Use @AutoWired or Constructor Injection (Just Two ways of doing it)
    public ExpenseAnalyticsService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository,
                          CurrentUserService currentUserService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    public List<RecentTransactionDTO> getRecentExpenseTransactions() {
        Integer userId = currentUserService.getCurrentUserId();
        List<Expense> recentExpenses = expenseRepository.findRecentExpenses(
                userId,
                PageRequest.of(0, 10)
        );
        List<RecentTransactionDTO> transactions = new ArrayList<>();
        for (Expense expense : recentExpenses) {
            transactions.add(
                    new RecentTransactionDTO(
                            "Expense",
                            expense.getTitle(),
                            expense.getAmount(),
                            expense.getDate()
                    )
            );
        }
        return transactions;
    }

    private Map<Integer, Map<Month, Double>> buildYearMonthMap(List<Expense> expenses) {
        Map<Integer, Map<Month, Double>> yearlyMap = new HashMap<>();
        for (Expense expense : expenses) {
            int year = expense.getDate().getYear();
            Month month = expense.getDate().getMonth();
            Map<Month, Double> monthMap =
                    yearlyMap.computeIfAbsent(year, y -> new HashMap<>());
            Double currentTotal = monthMap.get(month);
            if (currentTotal == null) {
                monthMap.put(month, expense.getAmount());
            } else {
                monthMap.put(month, currentTotal + expense.getAmount());
            }
        }
        return yearlyMap;
    }

    public List<MonthlyExpenseDTO> getMonthlyExpenseChart() {
        Integer userId = currentUserService.getCurrentUserId();
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        Map<Month, Double> monthTotals = new HashMap<>();
        for (Expense expense : expenses) {
            Month month = expense.getDate().getMonth();
            Double current = monthTotals.get(month);
            if (current == null) {
                monthTotals.put(month, expense.getAmount());
            } else {
                monthTotals.put(month, current + expense.getAmount());
            }
        }

        List<MonthlyExpenseDTO> monthlyExpenses = new ArrayList<>();
        for (Month month : Month.values()) {
            String monthName = month.name().substring(0, 3);
            Double total = monthTotals.get(month);
            if (total == null) {
                total = 0.0;
            }
            monthlyExpenses.add(new MonthlyExpenseDTO(monthName, total));
        }
        return monthlyExpenses;
    }

    public List<YearlyExpenseDTO> getYearlyExpenseChart() {
        Integer userId = currentUserService.getCurrentUserId();
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        Map<Integer, Map<Month, Double>> yearlyMap = buildYearMonthMap(expenses);
        List<YearlyExpenseDTO> yearlyExpenses = new ArrayList<>();
        for (Map.Entry<Integer, Map<Month, Double>> yearEntry : yearlyMap.entrySet()) {
            Integer year = yearEntry.getKey();
            Map<Month, Double> monthMap = yearEntry.getValue();
            YearlyExpenseDTO dto = new YearlyExpenseDTO();
            dto.setYear(year);
            List<MonthlyExpenseDTO> months = new ArrayList<>();
            for (Month month : Month.values()) {
                Double total = monthMap.get(month);
                if (total != null) {
                    String monthName = month.name().substring(0, 3);
                    months.add(new MonthlyExpenseDTO(monthName, total));
                }
            }
            dto.setMonths(months);
            yearlyExpenses.add(dto);
        }
        return yearlyExpenses;
    }

    public List<CategoryExpenseDTO> getCategoryExpenseBreakdown() {
        Integer userId = currentUserService.getCurrentUserId();
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        Map<String, Double> totals = new HashMap<>();
        double overallTotal = 0;

        // Calculate total amount for each category
        for (Expense expense : expenses) {
            String category = expense.getCategory().getName();
            if (totals.containsKey(category)) {
                double currentAmount = totals.get(category);
                totals.put(category, currentAmount + expense.getAmount());
            } else {
                totals.put(category, expense.getAmount());
            }
            overallTotal = overallTotal + expense.getAmount();
        }

        List<CategoryExpenseDTO> result = new ArrayList<>();
        // Create DTO objects
        for (String category : totals.keySet()) {
            double amount = totals.get(category);
            double percentage;
            if (overallTotal == 0) {
                percentage = 0;
            } else {
                percentage = (amount * 100) / overallTotal;
            }
            // Round to one decimal place
            percentage = Math.round(percentage * 10.0) / 10.0;
            CategoryExpenseDTO dto = new CategoryExpenseDTO(category, amount, percentage);
            result.add(dto);
        }
        result.sort(
                (a, b) -> Double.compare(b.getAmount(), a.getAmount())
        );
        return result;
    }

}
