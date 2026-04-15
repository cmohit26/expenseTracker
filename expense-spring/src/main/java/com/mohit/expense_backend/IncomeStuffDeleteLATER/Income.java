package com.mohit.expense_backend.IncomeStuffDeleteLATER;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohit.expense_backend.entities.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private LocalDate date;

    private String source;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Income() {}

    public Income(Double amount, LocalDate date, String source, User user) {
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.user = user;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
