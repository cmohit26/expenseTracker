package com.mohit.expense_backend.dto.DashboardDTOs;

import java.util.List;

public class DashboardDTO {

    private Double totalIncome;

    private Double totalExpenses;

    private Double currentBalance;

    private Integer categoryCount;

    private List<MonthlyExpenseDTO> monthlyExpenses;

    private List<YearlyExpenseDTO> yearlyExpenses;

    public List<YearlyExpenseDTO> getYearlyExpenses() {
        return yearlyExpenses;
    }

    public void setYearlyExpenses(List<YearlyExpenseDTO> yearlyExpenses) {
        this.yearlyExpenses = yearlyExpenses;
    }

    public List<MonthlyExpenseDTO> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(List<MonthlyExpenseDTO> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public List<RecentTransactionDTO> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<RecentTransactionDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    private List<RecentTransactionDTO> recentTransactions;

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Integer getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }
}