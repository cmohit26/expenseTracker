import axios from "axios";

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8082',
    withCredentials: true,
    headers: {
        common: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    }
});

export default axiosInstance