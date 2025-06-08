import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/admin',
    headers: {
        'Content-Type': 'application/json'
    }
});

export function loginAdmin(data) {
    return api.post('/login', data);
}

export function registerAdmin(data) {
    return api.post('/register', data);
}
