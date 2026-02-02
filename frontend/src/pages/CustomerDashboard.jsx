import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import TicketList from '../components/TicketList';
import TicketForm from '../components/TicketForm';
import '../styles/Dashboard.css';

export default function CustomerDashboard() {
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [refreshTrigger, setRefreshTrigger] = useState(0);
    const navigate = useNavigate();
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleTicketCreated = () => {
        setShowCreateForm(false);
        setRefreshTrigger(prev => prev + 1);
    };

    if (!user) return <div>Loading...</div>;

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <div className="header-content">
                    <h1>Customer Dashboard</h1>
                    <div className="user-info">
                        <span>Welcome, {user.firstName} {user.lastName}</span>
                        <button onClick={handleLogout} className="logout-btn">Logout</button>
                    </div>
                </div>
            </header>

            <div className="dashboard-content">
                <div className="actions-bar">
                    <h2>My Tickets</h2>
                    <button 
                        onClick={() => setShowCreateForm(!showCreateForm)}
                        className="create-ticket-btn"
                    >
                        {showCreateForm ? 'Cancel' : 'Create New Ticket'}
                    </button>
                </div>

                {showCreateForm && (
                    <TicketForm 
                        customerId={user.id}
                        onTicketCreated={handleTicketCreated}
                    />
                )}

                <TicketList 
                    customerId={user.id}
                    refreshTrigger={refreshTrigger}
                />
            </div>
        </div>
    );
}
