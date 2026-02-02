import React, { useState } from 'react';
import { ticketAPI } from '../services/apiService';
import '../styles/TicketForm.css';

export default function TicketForm({ customerId, onTicketCreated }) {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        priority: 'MEDIUM',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await ticketAPI.createTicket(
                customerId,
                formData.title,
                formData.description,
                formData.priority
            );

            // Reset form
            setFormData({
                title: '',
                description: '',
                priority: 'MEDIUM',
            });

            onTicketCreated();
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to create ticket');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="ticket-form-container">
            <h3>Create a New Ticket</h3>
            <form onSubmit={handleSubmit}>
                {error && <div className="error-message">{error}</div>}

                <div className="form-group">
                    <label htmlFor="title">Subject/Title:</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                        placeholder="Brief description of your issue"
                        maxLength="100"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                        placeholder="Detailed description of your issue"
                        rows="5"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="priority">Priority:</label>
                    <select
                        id="priority"
                        name="priority"
                        value={formData.priority}
                        onChange={handleChange}
                    >
                        <option value="LOW">Low</option>
                        <option value="MEDIUM">Medium</option>
                        <option value="HIGH">High</option>
                        <option value="URGENT">Urgent</option>
                    </select>
                </div>

                <button type="submit" disabled={loading} className="submit-btn">
                    {loading ? 'Creating...' : 'Create Ticket'}
                </button>
            </form>
        </div>
    );
}
