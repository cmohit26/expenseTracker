package com.mohit.expense_backend.dto.DashboardDTOs;

import java.util.List;

public class YearlyExpenseDTO {

    private Integer year;
    private List<MonthlyExpenseDTO> months;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<MonthlyExpenseDTO> getMonths() {
        return months;
    }

    public void setMonths(List<MonthlyExpenseDTO> months) {
        this.months = months;
    }
}
