package com.mohit.expense_backend.dto.DashboardDTOs;

public class CategoryExpenseDTO {

    private String name;
    private Double amount;
    private Double percentage;

    public CategoryExpenseDTO() {
    }

    public CategoryExpenseDTO(String name, Double amount, Double percentage) {
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}