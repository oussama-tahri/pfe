import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const ApiService = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Function to handle errors
const handleError = (error) => {
  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    console.error('Response Error:', error.response.data);
    return Promise.reject(error.response.data);
  } else if (error.request) {
    // The request was made but no response was received
    console.error('Request Error:', error.request);
    return Promise.reject(error.request);
  } else {
    // Something happened in setting up the request that triggered an Error
    console.error('Error:', error.message);
    return Promise.reject(error.message);
  }
};

const apiService = {
  // Function to import Excel file
  importExcel: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return ApiService.post('/api/import/excel', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => response.data)
    .catch(handleError);
  },

  // Function to update presence value
  updatePresence: (employeeId, date, newValue) => {
    return ApiService.put(`/presence/${employeeId}`, { date, newValue })
      .then(response => response.data)
      .catch(handleError);
  },

  // Function to calculate employee presence statistics
  calculateEmployeePresenceStatistics: () => {
    return ApiService.post('/api/statistics/employees')
      .then(response => response.data)
      .catch(handleError);
  },

  // Function to calculate squad presence statistics
  calculateSquadPresenceStatistics: () => {
    return ApiService.post('/api/statistics/squads')
      .then(response => response.data)
      .catch(handleError);
  },

  // Function to get squad presence percentage per week
  getSquadPresencePercentagePerWeek: (squadName, weekStartDate) => {
    return ApiService.get(`/api/statistics/presence-per-week/${squadName}/${weekStartDate}`)
      .then(response => response.data)
      .catch(handleError);
  },

  // Function to get squad presence percentage per month
  getSquadPresencePercentagePerMonth: (squadName, monthStartDate) => {
    return ApiService.get(`/api/statistics/presence-per-month/${squadName}/${monthStartDate}`)
      .then(response => response.data)
      .catch(handleError);
  },

  // Function to get employee presence status
  getEmployeePresenceStatus: (employeeName) => {
    return ApiService.get(`/api/statistics/presence-status?employeeName=${employeeName}`)
      .then(response => response.data)
      .catch(handleError);
  }
};

export default apiService;