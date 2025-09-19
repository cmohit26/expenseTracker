//Popular JavaScript library used to make HTTP requests from browsers
import axios from "axios"; 

// API endpoints used throughout the admin UI. Keeping explicit URLs for clarity.
const REST_API_GET_ALL_USERS_URL = "http://localhost:8082/allUsers";
const REST_API_GET_USER_BY_ID_URL = "http://localhost:8082/getUser";
const REST_API_UPDATE_USER_URL = "http://localhost:8082/editUser";
const REST_API_DELETE_USER_URL = "http://localhost:8082/deleteUser";
const REST_API_ADD_USER_URL = "http://localhost:8082/addUser";
const REST_API_CURRENT_USER_URL = "http://localhost:8082/currentUser";

// Fetch all users (requires credentials for session auth)
export const listOfUsers = () => axios.get(REST_API_GET_ALL_USERS_URL, {
  withCredentials: true
});

// Get currently authenticated user (null/401 when not logged in)
export const getCurrentUser = () => axios.get(REST_API_CURRENT_USER_URL, {
  withCredentials: true
});

// Log out current session
export const logoutUser = () => axios.post('http://localhost:8082/logout', {}, { withCredentials: true });

// CRUD helpers for user management
export const getUserById = (id) => axios.get(`${REST_API_GET_USER_BY_ID_URL}/${id}`);
export const updateUser = (id, userData) => axios.post(`${REST_API_UPDATE_USER_URL}/${id}`, userData);
export const deleteUser = (id) => axios.get(`${REST_API_DELETE_USER_URL}/${id}`);
export const addUser = (addedUserData) => axios.post(REST_API_ADD_USER_URL, addedUserData);

