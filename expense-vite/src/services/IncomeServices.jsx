import axios from "axios";

// Base API URL
const REST_API_INCOME_URL = "http://localhost:8080/v1/income";

// Create new income (authenticated user via @AuthenticationPrincipal)
export const addIncome = (incomeData) =>
  axios.post(REST_API_INCOME_URL, incomeData, {
    withCredentials: true,
  });

// Get all incomes for a specific user
export const getMyIncomes = () =>
  axios.get(`${REST_API_INCOME_URL}/me`, {
    withCredentials: true,
  });

// Update income by incomeId
export const updateIncome = (incomeId, incomeData) =>
  axios.put(`${REST_API_INCOME_URL}/${incomeId}`, incomeData, {
    withCredentials: true,
  });

// Delete income by incomeId
export const deleteIncome = (incomeId) =>
  axios.delete(`${REST_API_INCOME_URL}/${incomeId}`, {
    withCredentials: true,
  });