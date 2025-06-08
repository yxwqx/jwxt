import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/students',
    headers: {
        'Content-Type': 'application/json'
    }
});

export function fetchStudents() {
    return api.get();
}

export function addStudent(data) {
    return api.post('', data);
}

export function updateStudent(id, data) {
    return api.put(`/${id}`, data);
}

export function deleteStudent(id) {
    return api.delete(`/${id}`);
}
