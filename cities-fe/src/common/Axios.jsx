import axios from "axios";

export const API_URL = 'http://localhost:8080/api/v1'


export const Axios = axios.create({
    baseURL: API_URL,
    headers: {
        Accept: 'application/json',
    },
});

// Add a request interceptor
Axios.interceptors.request.use(function (config) {
    const accessToken = localStorage.getItem('accessToken');
    if(accessToken){
        config.headers.Authorization =  `Bearer ${accessToken}`;
    }

    return config;
});