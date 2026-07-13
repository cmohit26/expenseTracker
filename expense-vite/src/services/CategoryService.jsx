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


export const getAllCategories = () =>
axios.get(
    `${REST_API_CATEGORY_URL}/admin/all`,
    {
        withCredentials: true
    }
);

export const getUserCategories = () =>
axios.get(
    `${REST_API_CATEGORY_URL}/user/all`,
    {
        withCredentials: true
    }
);

export const getAvailableCategories = () =>
axios.get(
    `${REST_API_CATEGORY_URL}/user/available`,
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