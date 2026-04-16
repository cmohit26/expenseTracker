import axios from "axios";

const REST_API_CATEGORY_URL = "http://localhost:8080/v1/category";

// Create a new category
export const createCategory = (categoryData) =>
  axios.post(REST_API_CATEGORY_URL, categoryData);

// Get all categories
export const getAllCategories = () =>
  axios.get(REST_API_CATEGORY_URL, {
    withCredentials: true
  });

// Get category by ID
export const getCategoryById = (id) =>
  axios.get(`${REST_API_CATEGORY_URL}/${id}`, {
    withCredentials: true
  });

// Update category
export const updateCategory = (id, categoryData) =>
  axios.put(`${REST_API_CATEGORY_URL}/${id}`, categoryData);

// Delete category
export const deleteCategory = (id) =>
  axios.delete(`${REST_API_CATEGORY_URL}/${id}`);