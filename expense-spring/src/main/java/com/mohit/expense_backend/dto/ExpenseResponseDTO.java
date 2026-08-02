package com.mohit.expense_backend.dto;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;

import java.time.LocalDate;

public class ExpenseResponseDTO {

    private Integer id;

    private String title;

    private Double amount;

    private LocalDate date;

    private ApprovalStatus status;

    private String rejectionReason;

    private Integer categoryId;

    private String categoryName;


    public ExpenseResponseDTO( Integer id, String title, Double amount, LocalDate date,
            ApprovalStatus status, String rejectionReason, Integer categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}