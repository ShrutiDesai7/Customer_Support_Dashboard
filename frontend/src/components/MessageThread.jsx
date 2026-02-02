import React, { useState } from 'react';
import { messageAPI } from '../services/apiService';
import '../styles/MessageThread.css';

export default function MessageThread({ ticketId, messages, user, onMessageAdded }) {
    const [replyContent, setReplyContent] = useState('');
    const [noteContent, setNoteContent] = useState('');
    const [showNoteForm, setShowNoteForm] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleAddReply = async (e) => {
        e.preventDefault();
        if (!replyContent.trim()) return;

        setLoading(true);
        setError('');

        try {
            await messageAPI.addReply(ticketId, user.id, replyContent);
            setReplyContent('');
            onMessageAdded();
        } catch (err) {
            setError('Failed to add reply');
        } finally {
            setLoading(false);
        }
    };

    const handleAddNote = async (e) => {
        e.preventDefault();
        if (!noteContent.trim()) return;

        setLoading(true);
        setError('');

        try {
            await messageAPI.addNote(ticketId, user.id, noteContent);
            setNoteContent('');
            setShowNoteForm(false);
            onMessageAdded();
        } catch (err) {
            setError('Failed to add note');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="message-thread-container">
            <h3>Messages & Updates</h3>

            <div className="messages-list">
                {messages.length === 0 ? (
                    <p className="no-messages">No messages yet</p>
                ) : (
                    messages.map(message => (
                        <div
                            key={message.id}
                            className={`message ${message.messageType.toLowerCase()}`}
                        >
                            <div className="message-header">
                                <strong>{message.sender?.firstName} {message.sender?.lastName}</strong>
                                <span className="message-type">{message.messageType}</span>
                                <small>{new Date(message.createdAt).toLocaleString()}</small>
                            </div>
                            <div className="message-content">
                                {message.content}
                            </div>
                        </div>
                    ))
                )}
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="reply-form-container">
                <form onSubmit={handleAddReply}>
                    <textarea
                        value={replyContent}
                        onChange={(e) => setReplyContent(e.target.value)}
                        placeholder="Type your reply..."
                        rows="3"
                        disabled={loading}
                    />
                    <button type="submit" disabled={loading || !replyContent.trim()}>
                        {loading ? 'Sending...' : 'Send Reply'}
                    </button>
                </form>

                {user.role === 'AGENT' && (
                    <div className="note-section">
                        <button
                            type="button"
                            onClick={() => setShowNoteForm(!showNoteForm)}
                            className="toggle-note-btn"
                        >
                            {showNoteForm ? 'Cancel Note' : 'Add Internal Note'}
                        </button>

                        {showNoteForm && (
                            <form onSubmit={handleAddNote} className="note-form">
                                <textarea
                                    value={noteContent}
                                    onChange={(e) => setNoteContent(e.target.value)}
                                    placeholder="Internal note (only visible to agents)..."
                                    rows="3"
                                    disabled={loading}
                                />
                                <button type="submit" disabled={loading || !noteContent.trim()}>
                                    {loading ? 'Saving...' : 'Save Note'}
                                </button>
                            </form>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
