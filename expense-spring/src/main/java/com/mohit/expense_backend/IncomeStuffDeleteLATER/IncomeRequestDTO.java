package com.mohit.expense_backend.IncomeStuffDeleteLATER;

public class IncomeRequestDTO {
    private Double amount;
    private String source;
    private String date;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}