import axios from "axios";

const REST_API_CATEGORY_URL = "http://localhost:8080/v1/category";


// Create category
export const createCategory = (categoryData) =>
  axios.post(
    REST_API_CATEGORY_URL,
    categoryData,
    {
      withCredentials: true
    }
  );


// Get categories usable by current user
// Global + user's own categories
export const getAvailableCategories = () =>
  axios.get(
    `${REST_API_CATEGORY_URL}/available`,
    {
      withCredentials: true
    }
  );


// User only categories
export const getUserCategories = () =>
  axios.get(
    `${REST_API_CATEGORY_URL}/user`,
    {
      withCredentials: true
    }
  );


// Global categories
export const getGlobalCategories = () =>
  axios.get(
    `${REST_API_CATEGORY_URL}/global`,
    {
      withCredentials: true
    }
  );


// Get by ID
export const getCategoryById = (id) =>
  axios.get(
    `${REST_API_CATEGORY_URL}/${id}`,
    {
      withCredentials: true
    }
  );


// Update
export const updateCategory = (id, data) =>
  axios.put(
    `${REST_API_CATEGORY_URL}/${id}`,
    data,
    {
      withCredentials: true
    }
  );


// Delete
export const deleteCategory = (id) =>
  axios.delete(
    `${REST_API_CATEGORY_URL}/${id}`,
    {
      withCredentials: true
    }
  );