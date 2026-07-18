import axios from "axios";

const REST_API_ADMIN_URL = "http://localhost:8080/v1/admin";

// =========== EXPENSE REQUESTS

// Get all pending expenses
export const getPendingExpenses = () =>
  axios.get(`${REST_API_ADMIN_URL}/pending-expenses`, {
    withCredentials: true,
  });

// Approve an expense
export const approveExpense = (id) =>
  axios.put(
    `${REST_API_ADMIN_URL}/approve-expense/${id}`,
    {},
    {
      withCredentials: true,
    }
  );

// Reject an expense
export const rejectExpense = (id, reason) =>
  axios.put(
    `${REST_API_ADMIN_URL}/reject-expense/${id}`,
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
  axios.get(`${REST_API_ADMIN_URL}/pending-incomes`, {
    withCredentials: true,
  });

// Approve an income
export const approveIncome = (id) =>
  axios.put(
    `${REST_API_ADMIN_URL}/approve-income/${id}`,
    {},
    {
      withCredentials: true,
    }
  );

// Reject an income
export const rejectIncome = (id, reason) =>
  axios.put(
    `${REST_API_ADMIN_URL}/reject-income/${id}`,
    {
      reason: reason,
    },
    {
      withCredentials: true,
    }
  );