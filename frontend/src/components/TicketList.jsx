import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ticketAPI } from '../services/apiService';
import '../styles/TicketList.css';

export default function TicketList({ customerId, agentId, filterType = 'all', refreshTrigger = 0 }) {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchTickets();
    }, [customerId, agentId, filterType, refreshTrigger]);

    const fetchTickets = async () => {
        try {
            setLoading(true);
            let response;

            if (customerId) {
                response = await ticketAPI.getTicketsByCustomer(customerId);
            } else if (agentId) {
                if (filterType === 'assigned') {
                    response = await ticketAPI.getTicketsByAgent(agentId);
                } else if (filterType === 'unassigned') {
                    response = await ticketAPI.getUnassignedTickets();
                } else {
                    response = await ticketAPI.getAllTickets();
                }
            }

            setTickets(response.data || []);
        } catch (err) {
            setError('Failed to load tickets');
        } finally {
            setLoading(false);
        }
    };

    const getStatusColor = (status) => {
        switch (status) {
            case 'OPEN': return '#ff6b6b';
            case 'IN_PROGRESS': return '#ffd93d';
            case 'RESOLVED': return '#6bcf7f';
            case 'CLOSED': return '#868e96';
            default: return '#999';
        }
    };

    const getPriorityColor = (priority) => {
        switch (priority) {
            case 'LOW': return '#51cf66';
            case 'MEDIUM': return '#ffd93d';
            case 'HIGH': return '#ff922b';
            case 'URGENT': return '#ff6b6b';
            default: return '#999';
        }
    };

    if (loading) return <div className="loading">Loading tickets...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="ticket-list-container">
            {tickets.length === 0 ? (
                <p className="no-tickets">No tickets found</p>
            ) : (
                <div className="tickets-grid">
                    {tickets.map(ticket => (
                        <div
                            key={ticket.id}
                            className="ticket-card"
                            onClick={() => navigate(`/ticket/${ticket.id}`)}
                        >
                            <div className="ticket-header-card">
                                <h3>{ticket.title}</h3>
                                <span className="ticket-id">#{ticket.id}</span>
                            </div>

                            <p className="ticket-description">{ticket.description.substring(0, 100)}...</p>

                            <div className="ticket-badges">
                                <span
                                    className="badge status-badge"
                                    style={{ backgroundColor: getStatusColor(ticket.status) }}
                                >
                                    {ticket.status}
                                </span>
                                <span
                                    className="badge priority-badge"
                                    style={{ backgroundColor: getPriorityColor(ticket.priority) }}
                                >
                                    {ticket.priority}
                                </span>
                            </div>

                            <div className="ticket-footer">
                                <small>{new Date(ticket.createdAt).toLocaleDateString()}</small>
                                {ticket.assignedAgent && (
                                    <small className="agent-name">
                                        Agent: {ticket.assignedAgent.firstName}
                                    </small>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
