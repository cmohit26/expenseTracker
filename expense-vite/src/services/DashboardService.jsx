import axios from "axios";

const BASE_URL = "http://localhost:8080/v1/dashboard";

export const getDashboardData = () => {
  return axios.get(BASE_URL, {
    withCredentials: true,
  });
};