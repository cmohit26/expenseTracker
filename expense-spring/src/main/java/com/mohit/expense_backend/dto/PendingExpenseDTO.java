package com.mohit.expense_backend.dto;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;

import java.time.LocalDate;

public class PendingExpenseDTO {

    private Integer id;
    private String title;
    private Double amount;
    private LocalDate date;

    private String userFirstName;
    private String userLastName;

    private String categoryName;

    private ApprovalStatus status;

    private String rejectionReason;

    public PendingExpenseDTO() {
    }

    public PendingExpenseDTO(Integer id,
                             String title,
                             Double amount,
                             LocalDate date,
                             String userFirstName,
                             String userLastName,
                             String categoryName,
                             ApprovalStatus status,
                             String rejectionReason) {

        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.categoryName = categoryName;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}