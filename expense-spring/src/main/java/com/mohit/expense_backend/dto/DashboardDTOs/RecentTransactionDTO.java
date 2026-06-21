package com.mohit.expense_backend.dto.DashboardDTOs;

import java.time.LocalDate;

public class RecentTransactionDTO {

    private String type;
    private String title;
    private Double amount;
    private LocalDate date;

    public RecentTransactionDTO() {
    }

    public RecentTransactionDTO(String type, String title, Double amount, LocalDate date) {
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}