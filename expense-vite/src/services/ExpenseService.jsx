import axios from "axios";

const REST_API_EXPENSE_URL = "http://localhost:8080/v1/expense";

// Create a new expense
export const createExpense = (expenseData) =>
  axios.post(REST_API_EXPENSE_URL, expenseData);

// Get all expenses of a specific user
export const getExpensesByUser = (userId) =>
  axios.get(`${REST_API_EXPENSE_URL}/user/${userId}`, {
    withCredentials: true
  });

// Get recent 10 expenses of a user
export const getRecentExpenses = () =>
  axios.get("http://localhost:8080/v1/expense/recent", {
    withCredentials: true
  });

// Get expenses by category for a user
export const getExpensesByCategory = (userId, categoryId) =>
  axios.get(
    `${REST_API_EXPENSE_URL}/user/${userId}/category/${categoryId}`,
    {
      withCredentials: true
    }
  );

// Get a single expense by ID
export const getExpenseById = (id) =>
  axios.get(`${REST_API_EXPENSE_URL}/${id}`, {
    withCredentials: true
  });

// Update an expense
export const updateExpense = (id, expenseData) =>
  axios.put(`${REST_API_EXPENSE_URL}/${id}`, expenseData);

// Delete an expense
export const deleteExpense = (id) =>
  axios.delete(`${REST_API_EXPENSE_URL}/${id}`);