import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import CustomerDashboard from './pages/CustomerDashboard';
import AgentDashboard from './pages/AgentDashboard';
import TicketDetails from './pages/TicketDetails';
import './App.css';

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/customer-dashboard" element={<PrivateRoute><CustomerDashboard /></PrivateRoute>} />
                    <Route path="/agent-dashboard" element={<PrivateRoute><AgentDashboard /></PrivateRoute>} />
                    <Route path="/ticket/:id" element={<PrivateRoute><TicketDetails /></PrivateRoute>} />
                    <Route path="/" element={<Navigate to="/login" replace />} />
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
}
