package com.mohit.expense_backend.dto;

import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;

import java.time.LocalDate;

public class PendingIncomeDTO {

    private Integer id;

    private String source;

    private Double amount;

    private LocalDate date;

    private String userFirstName;

    private String userLastName;

    private ApprovalStatus status;

    private String rejectionReason;

    public PendingIncomeDTO() {
    }

    public PendingIncomeDTO(Integer id,
                            String source,
                            Double amount,
                            LocalDate date,
                            String userFirstName,
                            String userLastName,
                            ApprovalStatus status,
                            String rejectionReason) {

        this.id = id;
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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