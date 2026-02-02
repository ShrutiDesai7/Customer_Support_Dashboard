import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with base URL
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Add token to requests if it exists in localStorage
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Handle response errors (e.g., token expired)
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Token expired or unauthorized - clear local storage
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
            // Optionally redirect to login
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// User API calls
export const userAPI = {
    register: (email, password, firstName, lastName, role) => 
        apiClient.post('/users/register', { email, password, firstName, lastName, role }),
    
    login: (email, password) => 
        apiClient.post('/users/login', { email, password }),
    
    getUserById: (id) => 
        apiClient.get(`/users/${id}`),
    
    updateUser: (id, firstName, lastName) => 
        apiClient.put(`/users/${id}`, { firstName, lastName }),
    
    getAgents: () => 
        apiClient.get('/users/agents'),
    
    deactivateUser: (id) => 
        apiClient.delete(`/users/${id}`),
};

// Ticket API calls
export const ticketAPI = {
    createTicket: (customerId, title, description, priority) => 
        apiClient.post('/tickets', { customerId, title, description, priority }),
    
    getTicketById: (id) => 
        apiClient.get(`/tickets/${id}`),
    
    getTicketsByCustomer: (customerId) => 
        apiClient.get(`/tickets/customer/${customerId}`),
    
    getTicketsByAgent: (agentId) => 
        apiClient.get(`/tickets/agent/${agentId}`),
    
    getUnassignedTickets: () => 
        apiClient.get('/tickets/unassigned'),
    
    getAllTickets: () => 
        apiClient.get('/tickets'),
    
    updateTicketStatus: (id, status) => 
        apiClient.patch(`/tickets/${id}/status`, { status }),
    
    updateTicketPriority: (id, priority) => 
        apiClient.patch(`/tickets/${id}/priority`, { priority }),
    
    assignTicket: (id, agentId) => 
        apiClient.patch(`/tickets/${id}/assign`, { agentId }),
    
    unassignTicket: (id) => 
        apiClient.patch(`/tickets/${id}/unassign`),
};

// Message API calls
export const messageAPI = {
    addReply: (ticketId, senderId, content) => 
        apiClient.post('/messages/reply', { ticketId, senderId, content }),
    
    addNote: (ticketId, senderId, content) => 
        apiClient.post('/messages/note', { ticketId, senderId, content }),
    
    getTicketMessages: (ticketId) => 
        apiClient.get(`/messages/ticket/${ticketId}`),
    
    getUserMessages: (userId) => 
        apiClient.get(`/messages/user/${userId}`),
    
    getMessageById: (id) => 
        apiClient.get(`/messages/${id}`),
    
    deleteMessage: (id) => 
        apiClient.delete(`/messages/${id}`),
};

export default apiClient;
