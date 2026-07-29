package com.mohit.expense_backend.Services;

import com.mohit.expense_backend.Services.ExpenseServices.ExpenseAnalyticsService;
import com.mohit.expense_backend.Services.ExpenseServices.ExpenseService;
import com.mohit.expense_backend.dto.DashboardDTOs.DashboardDTO;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ExpenseAnalyticsService expenseAnalyticsService;

    public DashboardService(
            IncomeService incomeService,
            ExpenseService expenseService,
            ExpenseAnalyticsService expenseAnalyticsService1) {

        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.expenseAnalyticsService = expenseAnalyticsService1;
    }

    public DashboardDTO getDashboardData() {

        DashboardDTO dto = new DashboardDTO();

        double totalIncome = incomeService.getTotalIncome();
        double totalExpenses = expenseService.getTotalExpenses();

        dto.setTotalIncome(totalIncome);
        dto.setTotalExpenses(totalExpenses);
        dto.setCurrentBalance(totalIncome - totalExpenses);

//        Integer categoryCount = categoryRepository.getCategoryCount(userId);
//        dto.setCategoryCount(categoryCount);

        //Get the Recent Transactions
        dto.setRecentTransactions(expenseAnalyticsService.getRecentExpenseTransactions());

        //FOR THE MONTHLY CHART (Monthly Expense)
        dto.setMonthlyExpenses(expenseAnalyticsService.getMonthlyExpenseChart());

        //FOR THE Yearly CHART (Yearly Expense)
        dto.setYearlyExpenses(expenseAnalyticsService.getYearlyExpenseChart());

        //For the Category Bar Color Chart
        dto.setCategoryExpenses(expenseAnalyticsService.getCategoryExpenseBreakdown());

        return dto;
    }

}