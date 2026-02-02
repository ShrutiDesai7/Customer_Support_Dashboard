import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ticketAPI, messageAPI } from '../services/apiService';
import MessageThread from '../components/MessageThread';
import '../styles/TicketDetails.css';

export default function TicketDetails() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [ticket, setTicket] = useState(null);
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (!storedUser) {
            navigate('/login');
        } else {
            setUser(JSON.parse(storedUser));
        }
    }, [navigate]);

    useEffect(() => {
        fetchTicketDetails();
    }, [id]);

    const fetchTicketDetails = async () => {
        try {
            setLoading(true);
            const ticketResponse = await ticketAPI.getTicketById(id);
            setTicket(ticketResponse.data);

            const messagesResponse = await messageAPI.getTicketMessages(id);
            setMessages(messagesResponse.data);
        } catch (err) {
            setError('Failed to load ticket details');
        } finally {
            setLoading(false);
        }
    };

    const handleStatusChange = async (newStatus) => {
        try {
            await ticketAPI.updateTicketStatus(id, newStatus);
            setTicket(prev => ({ ...prev, status: newStatus }));
        } catch (err) {
            setError('Failed to update ticket status');
        }
    };

    const handlePriorityChange = async (newPriority) => {
        try {
            await ticketAPI.updateTicketPriority(id, newPriority);
            setTicket(prev => ({ ...prev, priority: newPriority }));
        } catch (err) {
            setError('Failed to update ticket priority');
        }
    };

    const handleMessageAdded = () => {
        fetchTicketDetails();
    };

    if (loading) return <div className="loading">Loading...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!ticket) return <div className="not-found">Ticket not found</div>;

    return (
        <div className="ticket-details-container">
            <button onClick={() => navigate(-1)} className="back-btn">← Back</button>

            <div className="ticket-header">
                <div className="ticket-title-section">
                    <h1>{ticket.title}</h1>
                    <p className="ticket-id">ID: #{ticket.id}</p>
                </div>
                <div className="ticket-status-section">
                    <select
                        value={ticket.status}
                        onChange={(e) => handleStatusChange(e.target.value)}
                        className={`status-select status-${ticket.status.toLowerCase()}`}
                    >
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="RESOLVED">Resolved</option>
                        <option value="CLOSED">Closed</option>
                    </select>
                </div>
            </div>

            <div className="ticket-meta">
                <div className="meta-item">
                    <strong>Priority:</strong>
                    <select
                        value={ticket.priority}
                        onChange={(e) => handlePriorityChange(e.target.value)}
                        className={`priority-select priority-${ticket.priority.toLowerCase()}`}
                    >
                        <option value="LOW">Low</option>
                        <option value="MEDIUM">Medium</option>
                        <option value="HIGH">High</option>
                        <option value="URGENT">Urgent</option>
                    </select>
                </div>
                <div className="meta-item">
                    <strong>Created:</strong>
                    <span>{new Date(ticket.createdAt).toLocaleDateString()}</span>
                </div>
                <div className="meta-item">
                    <strong>Customer:</strong>
                    <span>{ticket.customer?.firstName} {ticket.customer?.lastName}</span>
                </div>
                <div className="meta-item">
                    <strong>Assigned Agent:</strong>
                    <span>{ticket.assignedAgent ? `${ticket.assignedAgent.firstName} ${ticket.assignedAgent.lastName}` : 'Unassigned'}</span>
                </div>
            </div>

            <div className="ticket-description">
                <h3>Description</h3>
                <p>{ticket.description}</p>
            </div>

            <MessageThread
                ticketId={id}
                messages={messages}
                user={user}
                onMessageAdded={handleMessageAdded}
            />
        </div>
    );
}
