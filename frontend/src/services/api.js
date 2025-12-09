import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:3036/api', // La puerta de entrada a tu Java
    headers: {
        'Content-Type': 'application/json'
    }
});

export default api;