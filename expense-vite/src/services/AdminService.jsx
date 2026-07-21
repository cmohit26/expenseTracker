import axios from "axios";

const EXPENSE_URL = "http://localhost:8080/v1/expense";
const INCOME_URL = "http://localhost:8080/v1/income";

// =========== EXPENSE REQUESTS

// Get all pending expenses
export const getPendingExpenses = () =>
  axios.get(`${EXPENSE_URL}/pending-expenses`, {
    withCredentials: true,
  });

// Approve an expense
export const approveExpense = (id) =>
  axios.put(
    `${EXPENSE_URL}/approve-expense/${id}`,
    {},
    {
      withCredentials: true,
    }
  );

// Reject an expense
export const rejectExpense = (id, reason) =>
  axios.put(
    `${EXPENSE_URL}/reject-expense/${id}`,
    {
      reason: reason,
    },
    {
      withCredentials: true,
    }
  );

// =========== INCOME REQUESTS

// Get all pending incomes
export const getPendingIncomes = () =>
  axios.get(`${INCOME_URL}/pending-incomes`, {
    withCredentials: true,
  });

// Approve an income
export const approveIncome = (id) =>
  axios.put(
    `${INCOME_URL}/approve-income/${id}`,
    {},
    {
      withCredentials: true,
    }
  );

// Reject an income
export const rejectIncome = (id, reason) =>
  axios.put(
    `${INCOME_URL}/reject-income/${id}`,
    {
      reason: reason,
    },
    {
      withCredentials: true,
    }
  );