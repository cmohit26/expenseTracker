import axios from 'axios';

export const checkBackendHealth = async () => {
  try {
    const response = await axios.get('http://localhost:8080/manage/health');
    return response.status === 200;
  } catch (error) {
    return false;
  }
};

