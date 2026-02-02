import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import TicketList from '../components/TicketList';
import '../styles/Dashboard.css';

export default function AgentDashboard() {
    const [activeTab, setActiveTab] = useState('assigned');
    const navigate = useNavigate();
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    if (!user) return <div>Loading...</div>;

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <div className="header-content">
                    <h1>Agent Dashboard</h1>
                    <div className="user-info">
                        <span>Welcome, {user.firstName} {user.lastName}</span>
                        <button onClick={handleLogout} className="logout-btn">Logout</button>
                    </div>
                </div>
            </header>

            <div className="dashboard-content">
                <div className="tabs">
                    <button
                        className={`tab-btn ${activeTab === 'assigned' ? 'active' : ''}`}
                        onClick={() => setActiveTab('assigned')}
                    >
                        Assigned to Me
                    </button>
                    <button
                        className={`tab-btn ${activeTab === 'unassigned' ? 'active' : ''}`}
                        onClick={() => setActiveTab('unassigned')}
                    >
                        Unassigned
                    </button>
                    <button
                        className={`tab-btn ${activeTab === 'all' ? 'active' : ''}`}
                        onClick={() => setActiveTab('all')}
                    >
                        All Tickets
                    </button>
                </div>

                <TicketList
                    agentId={user.id}
                    filterType={activeTab}
                />
            </div>
        </div>
    );
}
